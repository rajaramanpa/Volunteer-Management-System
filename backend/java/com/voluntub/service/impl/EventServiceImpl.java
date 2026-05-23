package com.voluntub.service.impl;

import java.util.List;

import com.voluntub.service.OrganizerVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voluntub.dto.CreateEventRequest;
import com.voluntub.entity.Event;
import com.voluntub.entity.OrganizerProfile;
import com.voluntub.entity.User;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.OrganizerProfileRepo;
import com.voluntub.repository.UserRepository;
import com.voluntub.service.EmailService;
import com.voluntub.service.EventService;

@Service
public class EventServiceImpl implements EventService {
@Autowired
private EmailService emailService;
    private final EventRepository eventRepository;
    private final OrganizerProfileRepo organizerProfileRepo;
    private final UserRepository userRepository;
@Autowired
private  OrganizerVerificationService verificationService;

    public EventServiceImpl(EventRepository eventRepository,
                            OrganizerProfileRepo organizerProfileRepo,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.organizerProfileRepo = organizerProfileRepo;
        this.userRepository = userRepository;
    }

    /**
     * Ensure an OrganizerProfile exists for the given organizer user.
     * This is helpful for older databases where profiles were not created at registration time.
     */
    private OrganizerProfile getOrCreateOrganizerProfile(Long organizerUserId) {
        return organizerProfileRepo
                .findByUserId(organizerUserId)
                .orElseGet(() -> {
                    User user = userRepository.findById(organizerUserId)
                            .orElseThrow(() -> new RuntimeException("Organizer user not found"));

                    OrganizerProfile profile = new OrganizerProfile();
                    profile.setUser(user);
                    profile.setOrganizationName(user.getName());
                    profile.setDescription("");
                    return organizerProfileRepo.save(profile);
                });
    }

    @Override
    public Event createEvent(Long organizerId, CreateEventRequest request) {

        OrganizerProfile organizer = getOrCreateOrganizerProfile(organizerId);
        if (request.getRegisterDeadline() == null) {
            throw new RuntimeException("Registration deadline is required");
        }

        Event event = new Event();
        try {
            event.setTitle(request.getTitle());
            event.setCategory(request.getCategory());
            event.setDescription(request.getDescription());
            event.setStartDate(request.getStartDate());
            event.setEndDate(request.getEndDate());
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
            event.setLocationName(request.getLocationName());
            event.setAddress(request.getAddress());
            event.setCity(request.getCity());
            event.setArea(request.getArea());
            event.setRequiredVolunteers(request.getRequiredVolunteers());
            event.setSkillsRequired(request.getSkillsRequired());
            event.setMinAge(request.getMinAge());
            event.setGenderPreference(request.getGenderPreference());
            event.setRegisterDeadline(request.getRegisterDeadline());
            event.setOrganizer(organizer);

            return eventRepository.save(event);

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 THIS WILL EXPOSE THE REAL ERROR
            throw e;
        }

    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        OrganizerProfile organizer = getOrCreateOrganizerProfile(organizerId);

        return eventRepository.findByOrganizer(organizer);
    }
}
