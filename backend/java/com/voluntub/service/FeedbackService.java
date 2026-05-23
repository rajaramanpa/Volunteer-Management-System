package com.voluntub.service;

import com.voluntub.dto.FeedbackRequestDTO;

public interface FeedbackService {

    void submitFeedback(Long eventId, Long userId, FeedbackRequestDTO dto);
}
