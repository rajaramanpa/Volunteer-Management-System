package com.voluntub.service;

import com.voluntub.dto.VolunteerEventStatusDTO;
import com.voluntub.dto.VolunteerProfileDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.Volunteerprofile;

import java.util.List;

public interface VolunteerService {

    List<Event> getAvailableEvents(Long userId, String filter);
    VolunteerProfileDTO getProfile(Long userId);
    Volunteerprofile getVolunteerEntity(Long userId);
    VolunteerProfileDTO updateProfile(Long userId, VolunteerProfileDTO dto);

    List<VolunteerEventStatusDTO> getMyEventsWithStatus(Long userId);
    void joinEvent(Long eventId, Long userId);

}
