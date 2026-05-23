package com.voluntub.repository;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventFeedback;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventFeedbackRepository
        extends JpaRepository<EventFeedback, Long> {

    List<EventFeedback> findByEvent(Event event);
    Boolean existsByEventAndVolunteer(Event event, Volunteerprofile volunteer);
}
