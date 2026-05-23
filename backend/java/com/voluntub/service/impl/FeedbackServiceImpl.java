package com.voluntub.service.impl;

import com.voluntub.dto.FeedbackRequestDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventFeedback;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventFeedbackRepository;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final EventRepository eventRepo;
    private final VolunteerProfileRepo volunteerRepo;
    private final EventFeedbackRepository feedbackRepo;

    public FeedbackServiceImpl(
            EventRepository eventRepo,
            VolunteerProfileRepo volunteerRepo,
            EventFeedbackRepository feedbackRepo
    ) {
        this.eventRepo = eventRepo;
        this.volunteerRepo = volunteerRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public void submitFeedback(
            Long eventId,
            Long userId,
            FeedbackRequestDTO dto
    ) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        EventFeedback feedback = new EventFeedback();
        feedback.setEvent(event);
        feedback.setVolunteer(volunteer);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setSubmittedAt(LocalDateTime.now());

        feedbackRepo.save(feedback);
    }
}
