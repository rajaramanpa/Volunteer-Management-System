package com.voluntub.repository;

import com.voluntub.dto.VolunteerProfileDTO;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerProfileRepo extends JpaRepository<Volunteerprofile, Long> {

    Optional<Volunteerprofile> findByUserId(Long userId);

}
