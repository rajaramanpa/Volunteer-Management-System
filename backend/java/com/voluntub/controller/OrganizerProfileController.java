package com.voluntub.controller;

import com.voluntub.entity.OrganizerProfile;
import com.voluntub.repository.OrganizerProfileRepo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer/profile")
@CrossOrigin
public class OrganizerProfileController {

    private final OrganizerProfileRepo organizerProfileRepo;

    public OrganizerProfileController(OrganizerProfileRepo organizerProfileRepo) {
        this.organizerProfileRepo = organizerProfileRepo;
    }

    @GetMapping("/{userId}")
    public OrganizerProfile getProfile(@PathVariable Long userId) {
        return organizerProfileRepo
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Organizer profile not found"));
    }

}
