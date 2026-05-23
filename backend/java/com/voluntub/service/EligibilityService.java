package com.voluntub.service;

import com.voluntub.dto.EligibilityResultDTO;

public interface EligibilityService {

    EligibilityResultDTO checkEligibility(
            Long eventId,
            Long volunteerId
    );
}
