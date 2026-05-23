package com.voluntub.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntub.dto.CreateEventRequest;
import com.voluntub.dto.VolunteerRequestDTO;
import com.voluntub.entity.Event;
import com.voluntub.service.EventService;
import com.voluntub.service.OrganizerService;

import jakarta.validation.Valid;

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

    // ✅ restrict event creation to verified organizers
    @PostMapping("/{organizerId}")
    public Event createEvent(@PathVariable Long organizerId,
                             @Valid @RequestBody CreateEventRequest request) {
        if (!organizerService.isOrganizerVerified(organizerId)) {
            throw new IllegalStateException("Organizer is not verified. Please upload documents and wait for admin approval before creating events.");
        }
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



