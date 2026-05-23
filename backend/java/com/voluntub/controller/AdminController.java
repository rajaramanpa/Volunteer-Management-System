package com.voluntub.controller;

import com.voluntub.entity.Event;
import com.voluntub.entity.Role;
import com.voluntub.entity.User;
import com.voluntub.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------------- USERS ----------------

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/organizers")
    public List<User> getOrganizers() {
        return adminService.getOrganizers();
    }

    @GetMapping("/volunteers")
    public List<User> getVolunteers() {
        return adminService.getVolunteers();
    }

    // ---------------- EVENTS ----------------

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return adminService.getAllEvents();
    }

    @GetMapping("/events/pending")
    public List<Event> getPendingEvents() {
        return adminService.getPendingEvents();
    }

    @PutMapping("/events/{id}/approve")
    public void approveEvent(@PathVariable Long id) {
        adminService.approveEvent(id);
    }

    @PutMapping("/events/{id}/reject")
    public void rejectEvent(@PathVariable Long id) {
        adminService.rejectEvent(id);
    }
@DeleteMapping("/users/{id}")
public void deleteUser(@PathVariable Long id)
{
adminService.deleteUser(id);
}

    @GetMapping("/users/role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return adminService.getUsersByRole(role);
    }
    // ---------------- STATS ----------------

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return adminService.getStats();
    }
}
