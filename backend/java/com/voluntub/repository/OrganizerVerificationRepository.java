package com.voluntub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntub.entity.OrganizerProfile;
import com.voluntub.entity.OrganizerVerification;

public interface OrganizerVerificationRepository
        extends JpaRepository<OrganizerVerification, Long> {

    Optional<OrganizerVerification> findByOrganizer(OrganizerProfile organizer);

    List<OrganizerVerification> findByStatus(
            OrganizerVerification.Status status
    );
    List<OrganizerVerification> findAllByOrganizer(OrganizerProfile organizer);

    List<OrganizerVerification> findAllByOrganizerOrderByUploadedAtDesc(OrganizerProfile organizer);

}
