package com.voluntub.controller;

import com.voluntub.dto.EligibilityResultDTO;
import com.voluntub.service.EligibilityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer/eligibility")
@CrossOrigin
public class EligibilityController {

    private final EligibilityService eligibilityService;

    public EligibilityController(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    // ✅ CHECK ELIGIBILITY
    @GetMapping("/{eventId}/{volunteerId}")
    public EligibilityResultDTO checkEligibility(
            @PathVariable Long eventId,
            @PathVariable Long volunteerId
    ) {
        return eligibilityService.checkEligibility(eventId, volunteerId);
    }
}
