package com.voluntub.repository;

import com.voluntub.entity.OrganizerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizerProfileRepo extends JpaRepository<OrganizerProfile, Long> {
    Optional<OrganizerProfile> findByUserId(Long userId);
}
