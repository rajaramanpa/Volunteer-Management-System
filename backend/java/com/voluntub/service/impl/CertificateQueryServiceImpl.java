package com.voluntub.service.impl;

import com.voluntub.dto.VolunteerCertificateDTO;
import com.voluntub.entity.EventCertificate;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventCertificateRepository;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.CertificateQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateQueryServiceImpl implements CertificateQueryService {

    private final EventCertificateRepository certRepo;
    private final VolunteerProfileRepo volunteerRepo;

    public CertificateQueryServiceImpl(
            EventCertificateRepository certRepo,
            VolunteerProfileRepo volunteerRepo
    ) {
        this.certRepo = certRepo;
        this.volunteerRepo = volunteerRepo;
    }

    @Override
    public List<VolunteerCertificateDTO> getMyCertificates(Long userId) {

        Volunteerprofile volunteer = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        return certRepo.findByVolunteer(volunteer)
                .stream()
                .map(c -> new VolunteerCertificateDTO(
                        c.getEvent().getId(),
                        c.getEvent().getTitle(),
                        c.getIssuedDate(),
                        c.getCertificateUrl()
                ))
                .toList();
    }
}
