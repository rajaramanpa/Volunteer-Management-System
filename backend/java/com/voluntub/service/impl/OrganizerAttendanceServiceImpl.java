package com.voluntub.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voluntub.dto.ApprovedVolunteerDTO;
import com.voluntub.dto.AttendanceSummaryDTO;
import com.voluntub.dto.EligibilityResultDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventAttendance;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventAttendanceRepository;
import com.voluntub.repository.EventCertificateRepository;
import com.voluntub.repository.EventFeedbackRepository;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.EventVolunteerRepo;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.EligibilityService;
import com.voluntub.service.OrganizerAttendanceService;

@Service
public class OrganizerAttendanceServiceImpl
        implements OrganizerAttendanceService {

    private final EventAttendanceRepository attendanceRepo;
    private final EventRepository eventRepo;
    private final VolunteerProfileRepo volunteerRepo;
    @Autowired
    private EventFeedbackRepository feedbackRepo;
    @Autowired
    private EligibilityService eligibilityService;
    @Autowired
    private EventCertificateRepository certificateRepo;
private final EventVolunteerRepo eventVolunteerRepo;
    public OrganizerAttendanceServiceImpl(
            EventAttendanceRepository attendanceRepo,
            EventRepository eventRepo, EventVolunteerRepo eventVolunteerRepo,
            VolunteerProfileRepo volunteerRepo
    ) {
        this.attendanceRepo = attendanceRepo;
        this.eventRepo = eventRepo;
        this.volunteerRepo = volunteerRepo;
        this.eventVolunteerRepo=eventVolunteerRepo;
    }
    @Override
    public List<ApprovedVolunteerDTO> getApprovedVolunteers(Long eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return eventVolunteerRepo
                .findByEventAndStatus(event, "APPROVED")
                .stream()
                .map(ev -> new ApprovedVolunteerDTO(
                        ev.getVolunteer().getId(),
                        ev.getVolunteer().getUser().getName(),
                        ev.getVolunteer().getUser().getEmail()
                ))
                .toList();
    }

    @Override
    public List<ApprovedVolunteerDTO> getApprovedVolunteersForDate(Long eventId, LocalDate date) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return eventVolunteerRepo
                .findByEventAndStatus(event, "APPROVED")
                .stream()
                .map(ev -> {
                    Volunteerprofile volunteer = ev.getVolunteer();

                    ApprovedVolunteerDTO dto = new ApprovedVolunteerDTO(
                            volunteer.getId(),
                            volunteer.getUser().getName(),
                            volunteer.getUser().getEmail()
                    );

                    // Look up attendance record for this date (if any)
                    var existing = attendanceRepo.findByEventAndVolunteerAndAttendanceDate(
                            event,
                            volunteer,
                            date
                    );

                    existing.ifPresent(a -> {
                        dto.setStatus(a.getStatus());
                        dto.setAttendanceMarked(true);
                    });

                    return dto;
                })
                .toList();
    }

    @Override
    public void markAttendance(
            Long eventId,
            Long volunteerId,
            LocalDate date,
            String status
    ) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer = volunteerRepo.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // ✅ UPSERT instead of throwing error if already marked
        EventAttendance attendance = attendanceRepo
                .findByEventAndVolunteerAndAttendanceDate(event, volunteer, date)
                .orElseGet(() -> {
                    EventAttendance a = new EventAttendance();
                    a.setEvent(event);
                    a.setVolunteer(volunteer);
                    a.setAttendanceDate(date);
                    return a;
                });

        attendance.setStatus(status);

        attendanceRepo.save(attendance);
    }

    @Override
    public List<AttendanceSummaryDTO> getAttendanceSummary(Long eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // ✅ Only approved volunteers
        List<EventVolunteer> approvedVolunteers =
                eventVolunteerRepo.findByEventAndStatus(event, "APPROVED");

        List<AttendanceSummaryDTO> result = new ArrayList<>();

        for (EventVolunteer ev : approvedVolunteers) {

            Volunteerprofile volunteer = ev.getVolunteer();

            // ✅ REUSE existing eligibility logic
            EligibilityResultDTO eligibility =
                    eligibilityService.checkEligibility(
                            eventId,
                            volunteer.getId()
                    );

            boolean certIssued =
                    certificateRepo.existsByEventAndVolunteer(event, volunteer);

            boolean feedbackGiven =
                    feedbackRepo.existsByEventAndVolunteer(event, volunteer);

            AttendanceSummaryDTO dto = new AttendanceSummaryDTO();
            dto.setVolunteerId(volunteer.getId());
            dto.setName(volunteer.getUser().getName());
            dto.setEmail(volunteer.getUser().getEmail());
            dto.setAttendancePercentage(
                    eligibility.getAttendancePercentage()
            );
            dto.setEligible(eligibility.isEligible());
            dto.setCertificateIssued(certIssued);
            dto.setFeedbackGiven(feedbackGiven);

            result.add(dto);
        }

        return result;
    }

}
