package com.voluntub.service.impl;

import com.voluntub.dto.CreateEventRequest;
import com.voluntub.entity.Event;
import com.voluntub.entity.OrganizerProfile;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.OrganizerProfileRepo;
import com.voluntub.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final OrganizerProfileRepo organizerProfileRepo;

    public EventServiceImpl(EventRepository eventRepository,
                            OrganizerProfileRepo organizerProfileRepo) {
        this.eventRepository = eventRepository;
        this.organizerProfileRepo = organizerProfileRepo;
    }

    @Override
    public Event createEvent(Long organizerId, CreateEventRequest request) {

        OrganizerProfile organizer = organizerProfileRepo
                .findByUserId(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer profile not found"));
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
        OrganizerProfile organizer = organizerProfileRepo
                .findByUserId(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer profile not found"));

        return eventRepository.findByOrganizer(organizer);
    }
}
