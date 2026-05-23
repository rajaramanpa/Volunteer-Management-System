package com.voluntub.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.voluntub.dto.ApprovedVolunteerDTO;
import com.voluntub.dto.AttendanceSummaryDTO;
import com.voluntub.service.OrganizerAttendanceService;

@RestController
@RequestMapping("/api/organizer/attendance")
@CrossOrigin
public class OrganizerAttendanceController {

    private final OrganizerAttendanceService attendanceService;

    public OrganizerAttendanceController(
            OrganizerAttendanceService attendanceService
    ) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{eventId}/volunteers")
    public List<ApprovedVolunteerDTO> getApprovedVolunteers(
            @PathVariable Long eventId
    ) {
        return attendanceService.getApprovedVolunteers(eventId);
    }

    @GetMapping("/{eventId}/volunteers/status")
    public List<ApprovedVolunteerDTO> getApprovedVolunteersForDate(
            @PathVariable Long eventId,
            @RequestParam String date
    ) {
        LocalDate d = LocalDate.parse(date);
        return attendanceService.getApprovedVolunteersForDate(eventId, d);
    }

    @PostMapping("/{eventId}")
    public void markAttendance(
            @PathVariable Long eventId,
            @RequestBody Map<String, String> body
    ) {
        Long volunteerId = Long.parseLong(body.get("volunteerId"));
        LocalDate date = LocalDate.parse(body.get("date"));
        String status = body.get("status");

        attendanceService.markAttendance(
                eventId,
                volunteerId,
                date,
                status
        );
    }

    @GetMapping("/{eventId}/summary")
    public List<AttendanceSummaryDTO> getAttendanceSummary(
            @PathVariable Long eventId
    ) {
        return attendanceService.getAttendanceSummary(eventId);
    }


}
