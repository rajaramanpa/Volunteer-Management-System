package com.voluntub.repository;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventCertificate;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventCertificateRepository
        extends JpaRepository<EventCertificate, Long> {

    Optional<EventCertificate>
    findByEventAndVolunteer(Event event, Volunteerprofile volunteer);
    List<EventCertificate> findByVolunteer(Volunteerprofile volunteer);
Boolean existsByEventAndVolunteer(Event event,Volunteerprofile volunteer);
    Optional<EventCertificate>
    findByEventIdAndVolunteerUserId(Long eventId, Long userId);

}
