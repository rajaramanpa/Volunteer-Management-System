package com.voluntub.service.impl;

import com.voluntub.entity.Event;
import com.voluntub.entity.Event.EventStatus;
import com.voluntub.entity.Role;
import com.voluntub.entity.User;
import com.voluntub.repository.*;
import com.voluntub.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
@Autowired
private  VolunteerProfileRepo volunteerProfileRepo;
@Autowired
private  OrganizerProfileRepo organizerProfileRepo;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Autowired
    private EventVolunteerRepo eventVolunteerRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // ---------------- USERS ----------------

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getOrganizers() {
        return userRepository.findByRole(Role.ORGANIZER);
    }

    @Override
    public List<User> getVolunteers() {
        return userRepository.findByRole(Role.VOLUNTEER);
    }

    // ---------------- EVENTS ----------------

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getPendingEvents() {
        return eventRepository.findByStatus(Event.EventStatus.PENDING);
    }
    @Transactional
    @Override
    public void approveEvent(Long id) {
        int updated = eventRepository.updateStatus(
                id, Event.EventStatus.APPROVED);

        if (updated == 0) {
            throw new RuntimeException("Event not found");
        }
    }

    @Transactional
    @Override
    public void rejectEvent(Long id) {
        int updated = eventRepository.updateStatus(
                id, Event.EventStatus.REJECTED);

        if (updated == 0) {
            throw new RuntimeException("Event not found");
        }
    }
    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin cannot be deleted");
        }

// 1️⃣ If volunteer → delete joins + profile
        volunteerProfileRepo.findByUserId(userId).ifPresent(volunteer -> {

            eventVolunteerRepository.deleteAll(
                    eventVolunteerRepository.findByVolunteer(volunteer)
            );

            volunteerProfileRepo.delete(volunteer);
        });

// 2️⃣ If organizer → delete events + organizer profile
        organizerProfileRepo.findByUserId(userId).ifPresent(organizer -> {

            List<Event> events = eventRepository.findByOrganizer(organizer);

            // delete joins for organizer events
            events.forEach(event ->
                    eventVolunteerRepository.deleteAll(
                            eventVolunteerRepository.findByEvent(event)
                    )
            );

            eventRepository.deleteAll(events);
            organizerProfileRepo.delete(organizer);
        });

        // 3️⃣ Finally delete user
        userRepository.delete(user);
    }
    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
    // ---------------- STATS ----------------

    @Override
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("organizers", userRepository.countByRole(Role.ORGANIZER));
        stats.put("volunteers", userRepository.countByRole(Role.VOLUNTEER));
        stats.put("events", eventRepository.count());
        stats.put("pendingEvents",
                eventRepository.countByStatus(Event.EventStatus.PENDING));

        return stats;
    }
}
