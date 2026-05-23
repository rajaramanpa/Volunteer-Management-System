package com.voluntub.service;

import java.time.LocalDate;
import java.util.List;

import com.voluntub.dto.ApprovedVolunteerDTO;
import com.voluntub.dto.AttendanceSummaryDTO;

public interface OrganizerAttendanceService {

    void markAttendance(
            Long eventId,
            Long volunteerId,
            LocalDate date,
            String status
    );
    List<ApprovedVolunteerDTO> getApprovedVolunteers(Long eventId);
    List<ApprovedVolunteerDTO> getApprovedVolunteersForDate(Long eventId, LocalDate date);
    List<AttendanceSummaryDTO> getAttendanceSummary(Long eventId);

}
