package com.voluntub.service;

import com.voluntub.dto.CreateEventRequest;
import com.voluntub.entity.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Long organizerId, CreateEventRequest request);

    List<Event> getEventsByOrganizer(Long organizerId);
}
