package com.voluntub.repository;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventVolunteerRepo
        extends JpaRepository<EventVolunteer, Long> {
    Optional<EventVolunteer> findByEventAndVolunteer(Event event, Volunteerprofile volunteer);

    List<EventVolunteer> findByVolunteer(Volunteerprofile volunteer);

    List<EventVolunteer> findByEvent(Event event);

    long countByEventAndStatus(Event event, String status);
}
