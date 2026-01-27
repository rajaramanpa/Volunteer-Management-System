package com.voluntub.service;

import com.voluntub.dto.VolunteerEventStatusDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.Volunteerprofile;

import java.util.List;

public interface VolunteerService {
    Volunteerprofile getProfile(Long userId);
    List<Event> getAvailableEvents(Long userId, String filter);


    List<VolunteerEventStatusDTO> getMyEventsWithStatus(Long userId);
    void joinEvent(Long eventId, Long userId);

}
