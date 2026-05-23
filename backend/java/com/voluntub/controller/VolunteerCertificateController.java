package com.voluntub.controller;

import com.voluntub.entity.EventCertificate;
import com.voluntub.repository.EventCertificateRepository;
import com.voluntub.service.CertificatePdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteer/certificates")
@CrossOrigin
public class VolunteerCertificateController {

    private final EventCertificateRepository certRepo;
    private final CertificatePdfService pdfService;

    public VolunteerCertificateController(
            EventCertificateRepository certRepo,
            CertificatePdfService pdfService
    ) {
        this.certRepo = certRepo;
        this.pdfService = pdfService;
    }

    @GetMapping("/download/{eventId}/{userId}")
    public ResponseEntity<byte[]> download(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {

        EventCertificate cert = certRepo
                .findByEventIdAndVolunteerUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Certificate not issued"));

        byte[] pdf = pdfService.generateCertificate(
                cert.getVolunteer().getUser().getName(),
                cert.getEvent().getTitle(),
                cert.getIssuedDate().toString()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
