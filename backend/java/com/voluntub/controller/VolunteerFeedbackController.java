package com.voluntub.controller;

import com.voluntub.dto.FeedbackRequestDTO;
import com.voluntub.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteer/feedback")
@CrossOrigin
public class VolunteerFeedbackController {

    private final FeedbackService feedbackService;

    public VolunteerFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/{eventId}/{userId}")
    public void submitFeedback(
            @PathVariable Long eventId,
            @PathVariable Long userId,
            @RequestBody FeedbackRequestDTO dto
    ) {
        feedbackService.submitFeedback(eventId, userId, dto);
    }
}
