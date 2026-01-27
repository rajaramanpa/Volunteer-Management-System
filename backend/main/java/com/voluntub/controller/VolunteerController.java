package com.voluntub.controller;

import com.voluntub.dto.VolunteerEventStatusDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.service.VolunteerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteer")
@CrossOrigin
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/profile/{userId}")
    public Volunteerprofile getProfile(@PathVariable Long userId) {
        return volunteerService.getProfile(userId);
    }
    @GetMapping("/events")
    public List<Event> getAvailableEvents(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String filter
    ) {
        return volunteerService.getAvailableEvents(userId, filter);
    }


    @GetMapping("/my-events/{userId}")
    public List<VolunteerEventStatusDTO> getMyEvents(@PathVariable Long userId) {
        return volunteerService.getMyEventsWithStatus(userId);
    }

    @PostMapping("/events/{eventId}/join/{userId}")
    public void joinEvent(@PathVariable Long eventId,
                          @PathVariable Long userId) {
        volunteerService.joinEvent(eventId, userId);
    }
}
