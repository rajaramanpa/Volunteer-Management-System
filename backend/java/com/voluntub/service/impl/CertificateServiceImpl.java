package com.voluntub.service.impl;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventCertificate;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventCertificateRepository;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.CertificateService;
import com.voluntub.service.EligibilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final EventRepository eventRepo;
    private final VolunteerProfileRepo volunteerRepo;
    private final EventCertificateRepository certificateRepo;
    private final EligibilityService eligibilityService;

    public CertificateServiceImpl(
            EventRepository eventRepo,
            VolunteerProfileRepo volunteerRepo,
            EventCertificateRepository certificateRepo,
            EligibilityService eligibilityService
    ) {
        this.eventRepo = eventRepo;
        this.volunteerRepo = volunteerRepo;
        this.certificateRepo = certificateRepo;
        this.eligibilityService = eligibilityService;
    }

    @Override
    public void issueCertificate(Long eventId, Long volunteerId) {

        var eligibility =
                eligibilityService.checkEligibility(eventId, volunteerId);

        if (!eligibility.isEligible()) {
            throw new RuntimeException("Volunteer not eligible");
        }

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer =
                volunteerRepo.findById(volunteerId)
                        .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        certificateRepo
                .findByEventAndVolunteer(event, volunteer)
                .ifPresent(c -> {
                    throw new RuntimeException("Certificate already issued");
                });

        EventCertificate cert = new EventCertificate();
        cert.setEvent(event);
        cert.setVolunteer(volunteer);
        cert.setIssuedDate(LocalDate.now());
        cert.setCertificateUrl(
                "CERT-" + eventId + "-" + volunteerId
        );

        certificateRepo.save(cert);
    }
}
