package com.voluntub.service;

import com.voluntub.dto.VolunteerRequestDTO;
import com.voluntub.entity.EventVolunteer;

import java.util.List;

public interface OrganizerService {
    void approveVolunteer(Long eventVolunteerId);
    void rejectVolunteer(Long eventVolunteerId);
    public List<VolunteerRequestDTO> getRequestsForEvent(Long eventId);
}

