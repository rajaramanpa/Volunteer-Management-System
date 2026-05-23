package com.voluntub.service;

import java.util.List;

import com.voluntub.dto.VolunteerRequestDTO;

public interface OrganizerService {
    void approveVolunteer(Long eventVolunteerId);
    void rejectVolunteer(Long eventVolunteerId);
    List<VolunteerRequestDTO> getRequestsForEvent(Long eventId);

    boolean isOrganizerVerified(Long organizerUserId);
}

