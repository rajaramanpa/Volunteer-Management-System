package com.voluntub.service.impl;

import com.voluntub.dto.VolunteerRequestDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.EventVolunteerRepo;
import com.voluntub.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerServiceImpl implements OrganizerService {

    private final EventVolunteerRepo eventVolunteerRepo;

    public OrganizerServiceImpl(EventVolunteerRepo eventVolunteerRepo) {
        this.eventVolunteerRepo = eventVolunteerRepo;
    }
@Autowired
private  EventRepository eventRepo;

    @Override
    public void approveVolunteer(Long requestId) {

        EventVolunteer ev = eventVolunteerRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Event event = ev.getEvent();

        long approvedCount = eventVolunteerRepo
                .countByEventAndStatus(event, "APPROVED");

        if (approvedCount >= event.getRequiredVolunteers()) {
            throw new RuntimeException("Volunteer limit reached");
        }

        ev.setStatus("APPROVED");
        eventVolunteerRepo.save(ev);
    }

    @Override
    public void rejectVolunteer(Long id) {
        EventVolunteer ev = eventVolunteerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ev.setStatus("REJECTED");

        eventVolunteerRepo.save(ev);
    }
    @Override
    public List<VolunteerRequestDTO> getRequestsForEvent(Long eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return eventVolunteerRepo.findByEvent(event)
                .stream()
                .map(ev -> {
                    VolunteerRequestDTO dto = new VolunteerRequestDTO();

                    dto.setRequestId(ev.getId());
                    dto.setStatus(ev.getStatus());
                    dto.setRequestedAt(ev.getRequestedAt());

                    dto.setVolunteerId(ev.getVolunteer().getId());
                    dto.setVolunteerName(ev.getVolunteer().getUser().getName());
                    dto.setVolunteerEmail(ev.getVolunteer().getUser().getEmail());
                    dto.setSkills(ev.getVolunteer().getSkills());
                    dto.setAvailability(ev.getVolunteer().getAvailability());
                    return dto;
                })
                .toList();
    }

}
