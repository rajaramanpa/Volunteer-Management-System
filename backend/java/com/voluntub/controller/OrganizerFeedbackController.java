package com.voluntub.controller;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventFeedback;
import com.voluntub.repository.EventFeedbackRepository;
import com.voluntub.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizer/feedback")
@CrossOrigin
public class OrganizerFeedbackController {

    private final EventRepository eventRepo;
    private final EventFeedbackRepository feedbackRepo;

    public OrganizerFeedbackController(
            EventRepository eventRepo,
            EventFeedbackRepository feedbackRepo
    ) {
        this.eventRepo = eventRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @GetMapping("/{eventId}")
    public List<EventFeedback> getFeedbackForEvent(
            @PathVariable Long eventId
    ) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return feedbackRepo.findByEvent(event);
    }
}
