package com.voluntub.service.impl;

import com.voluntub.dto.EligibilityResultDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventAttendance;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventAttendanceRepository;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.EligibilityService;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EligibilityServiceImpl implements EligibilityService {

    private final EventRepository eventRepo;
    private final VolunteerProfileRepo volunteerRepo;
    private final EventAttendanceRepository attendanceRepo;

    public EligibilityServiceImpl(
            EventRepository eventRepo,
            VolunteerProfileRepo volunteerRepo,
            EventAttendanceRepository attendanceRepo
    ) {
        this.eventRepo = eventRepo;
        this.volunteerRepo = volunteerRepo;
        this.attendanceRepo = attendanceRepo;
    }

    @Override
    public EligibilityResultDTO checkEligibility(
            Long eventId,
            Long volunteerId
    ) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer = volunteerRepo.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        long totalDays =
                ChronoUnit.DAYS.between(
                        event.getStartDate(),
                        event.getEndDate()
                ) + 1;

        List<EventAttendance> records =
                attendanceRepo.findByEventAndVolunteer(event, volunteer);

        long presentDays = records.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        double percentage =
                (presentDays * 100.0) / totalDays;

        boolean eligible = percentage >= 75.0;

        return new EligibilityResultDTO(
                volunteer.getId(),
                Math.round(percentage * 100.0) / 100.0,
                eligible
        );
    }
}

