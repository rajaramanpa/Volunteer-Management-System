package com.voluntub.controller;

import com.voluntub.dto.CreateEventRequest;
import com.voluntub.dto.VolunteerRequestDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.service.EventService;
import com.voluntub.service.OrganizerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer/events")
@CrossOrigin
public class OrganizerEventController {

    private final EventService eventService;
    private final OrganizerService organizerService;

    public OrganizerEventController(
            EventService eventService,
            OrganizerService organizerService
    ) {
        this.eventService = eventService;
        this.organizerService = organizerService;
    }

    // ✅ existing endpoints stay untouched
    @PostMapping("/{organizerId}")
    public Event createEvent(@PathVariable Long organizerId,
                             @Valid @RequestBody CreateEventRequest request) {
        return eventService.createEvent(organizerId, request);
    }

    @GetMapping("/{organizerId}")
    public List<Event> getMyEvents(@PathVariable Long organizerId) {
        return eventService.getEventsByOrganizer(organizerId);
    }

    @PutMapping("/requests/{requestId}/approve")
    public void approveVolunteer(@PathVariable Long requestId) {
        organizerService.approveVolunteer(requestId);
    }

    @PutMapping("/requests/{requestId}/reject")
    public void rejectVolunteer(@PathVariable Long requestId) {
        organizerService.rejectVolunteer(requestId);
    }
    @GetMapping("/{eventId}/requests")
    public List<VolunteerRequestDTO> getEventRequests(@PathVariable Long eventId) {
        return organizerService.getRequestsForEvent(eventId);
    }

}



