package com.voluntub.service.impl;

import com.voluntub.dto.LoginRequest;
import com.voluntub.dto.LoginResponse;
import com.voluntub.dto.RegisterRequest;
import com.voluntub.entity.*;
import com.voluntub.repository.*;
import com.voluntub.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VolunteerProfileRepo volunteerProfileRepo;
    private final OrganizerProfileRepo organizerProfileRepo;

    public UserServiceImpl(UserRepository userRepository,
                           VolunteerProfileRepo volunteerProfileRepo,
                           OrganizerProfileRepo organizerProfileRepo) {
        this.userRepository = userRepository;
        this.volunteerProfileRepo = volunteerProfileRepo;
        this.organizerProfileRepo = organizerProfileRepo;
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // plain for now
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.VOLUNTEER) {
            Volunteerprofile vp = new Volunteerprofile();
            vp.setUser(savedUser);
            volunteerProfileRepo.save(vp);
        }

        if (savedUser.getRole() == Role.ORGANIZER) {
            OrganizerProfile op = new OrganizerProfile();
            op.setUser(savedUser);
            organizerProfileRepo.save(op);
        }

        return savedUser;
    }
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

}
