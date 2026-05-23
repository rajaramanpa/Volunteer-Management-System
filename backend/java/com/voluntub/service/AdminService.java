package com.voluntub.service;

import com.voluntub.entity.Event;
import com.voluntub.entity.Role;
import com.voluntub.entity.User;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // Users
    List<User> getAllUsers();
    List<User> getOrganizers();
    List<User> getVolunteers();

    // Events
    List<Event> getAllEvents();
    List<Event> getPendingEvents();

    void approveEvent(Long eventId);
    void rejectEvent(Long eventId);
 void deleteUser(Long UserId);
    List<User> getUsersByRole(Role role);
    // Dashboard stats
    Map<String, Long> getStats();
}
