package com.voluntub.service.impl;

import com.voluntub.dto.VolunteerEventStatusDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.EventVolunteerRepo;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerProfileRepo volunteerRepo;
    private final EventRepository eventRepo;
@Autowired
private EventVolunteerRepo eventVolunteerRepo;
    public VolunteerServiceImpl(VolunteerProfileRepo volunteerRepo,
                                EventRepository eventRepo) {
        this.volunteerRepo = volunteerRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public Volunteerprofile getProfile(Long userId) {
        return volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer profile not found"));
    }
    @Override
    public List<Event> getAvailableEvents(Long userId, String filter) {

        Volunteerprofile volunteer = getProfile(userId);

        // 🔹 events already requested/joined
        List<Long> joinedEventIds = eventVolunteerRepo
                .findByVolunteer(volunteer)
                .stream()
                .map(ev -> ev.getEvent().getId())
                .toList();

        List<Event> events = eventRepo.findAll();

        // 🔹 exclude joined events
        List<Event> availableEvents = events.stream()
                .filter(e -> !joinedEventIds.contains(e.getId()))
                .toList();

        LocalDate today = LocalDate.now();

        return switch (filter.toUpperCase()) {

            case "LIVE" -> availableEvents.stream()
                    .filter(e ->
                            !today.isBefore(e.getStartDate()) &&
                                    !today.isAfter(e.getEndDate())
                    )
                    .toList();

            case "UPCOMING" -> availableEvents.stream()
                    .filter(e -> today.isBefore(e.getStartDate()))
                    .toList();

            case "COMPLETED" -> availableEvents.stream()
                    .filter(e -> today.isAfter(e.getEndDate()))
                    .toList();

            default -> availableEvents;
        };
    }



    @Override
    public List<VolunteerEventStatusDTO> getMyEventsWithStatus(Long userId) {

        Volunteerprofile volunteer = getProfile(userId);

        return eventVolunteerRepo.findByVolunteer(volunteer)
                .stream()
                .map(ev -> {
                    VolunteerEventStatusDTO dto = new VolunteerEventStatusDTO();

                    dto.setEventId(ev.getEvent().getId());
                    dto.setTitle(ev.getEvent().getTitle());
                    dto.setCity(ev.getEvent().getCity());
                    dto.setArea(ev.getEvent().getArea());
                    dto.setStartDate(ev.getEvent().getStartDate());
                    dto.setEndDate(ev.getEvent().getEndDate());

                    dto.setStatus(ev.getStatus());

                    return dto;
                })
                .toList();
    }


    @Override
    public void joinEvent(Long eventId, Long userId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer profile not found"));

        // ❌ check via EventVolunteer (NOT event.getVolunteers)
        eventVolunteerRepo.findByEventAndVolunteer(event, volunteer)
                .ifPresent(ev -> {
                    throw new RuntimeException("Already requested");
                });

        EventVolunteer ev = new EventVolunteer();
        ev.setEvent(event);
        ev.setVolunteer(volunteer);
        ev.setStatus("PENDING");
        ev.setRequestedAt(LocalDateTime.now());
        if (LocalDate.now().isAfter(event.getRegisterDeadline())) {
            throw new RuntimeException("Registration deadline has passed");
        }

        eventVolunteerRepo.save(ev);
    }


}
