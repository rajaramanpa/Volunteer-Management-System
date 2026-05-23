package com.voluntub.controller;

import org.springframework.core.io.Resource;
import com.voluntub.entity.OrganizerVerification;
import com.voluntub.repository.OrganizerVerificationRepository;
import com.voluntub.service.OrganizerVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/organizer/verification")
@CrossOrigin
public class OrganizerVerificationController {

    private final OrganizerVerificationService service;
    private final OrganizerVerificationRepository repo;

    public OrganizerVerificationController(
            OrganizerVerificationService service,
            OrganizerVerificationRepository repo
    ) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/{organizerId}/upload")
    public void upload(@PathVariable Long organizerId,
                       @RequestParam("file") MultipartFile file) {
        service.uploadDocument(organizerId, file);
    }

    @GetMapping("/{organizerId}")
    public List<OrganizerVerification> myDocuments(@PathVariable Long organizerId) {
        return service.getMyDocuments(organizerId);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> view(@PathVariable Long id) {
        return service.viewFile(id);
    }
}
