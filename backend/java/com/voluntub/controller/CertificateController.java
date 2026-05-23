package com.voluntub.controller;

import com.voluntub.service.CertificateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer/certificates")
@CrossOrigin
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/issue/{eventId}/{volunteerId}")
    public void issueCertificate(
            @PathVariable Long eventId,
            @PathVariable Long volunteerId
    ) {
        certificateService.issueCertificate(eventId, volunteerId);
    }
}
