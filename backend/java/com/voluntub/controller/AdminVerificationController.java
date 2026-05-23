package com.voluntub.controller;

import org.springframework.core.io.Resource;
import com.voluntub.dto.OrganizerDocumentDTO;
import com.voluntub.service.OrganizerVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/verification")
@CrossOrigin
public class AdminVerificationController {

    private final OrganizerVerificationService service;

    public AdminVerificationController(OrganizerVerificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrganizerDocumentDTO> all() {
        return service.getAllVerificationDTOs();
    }

    @PutMapping("/{id}/{status}")
    public void review(@PathVariable Long id,
                       @PathVariable String status,
                       @RequestParam(required = false) String remark) {
        service.reviewVerification(id, status, remark);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> view(@PathVariable Long id) {
        return service.viewFile(id);
    }
}
