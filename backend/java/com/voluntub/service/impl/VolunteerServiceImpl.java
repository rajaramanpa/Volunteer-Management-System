package com.voluntub.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voluntub.dto.EligibilityResultDTO;
import com.voluntub.dto.VolunteerEventStatusDTO;
import com.voluntub.dto.VolunteerProfileDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.entity.Volunteerprofile;
import com.voluntub.repository.EventCertificateRepository;
import com.voluntub.repository.EventFeedbackRepository;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.EventVolunteerRepo;
import com.voluntub.repository.VolunteerProfileRepo;
import com.voluntub.service.EligibilityService;
import com.voluntub.service.EmailService;
import com.voluntub.service.VolunteerService;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerProfileRepo volunteerRepo;
    private final EventRepository eventRepo;
    @Autowired
    private EventVolunteerRepo eventVolunteerRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EligibilityService eligibilityService;
    @Autowired
    private EventCertificateRepository certificateRepo;
    @Autowired
    private EventFeedbackRepository feedbackRepo;
    public VolunteerServiceImpl(VolunteerProfileRepo volunteerRepo,
                                EventRepository eventRepo) {
        this.volunteerRepo = volunteerRepo;
        this.eventRepo = eventRepo;
    }
    @Override
    public Volunteerprofile getVolunteerEntity(Long userId) {
        return volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer profile not found"));
    }


    @Override
    public List<Event> getAvailableEvents(Long userId, String filter) {

        Volunteerprofile volunteer = getVolunteerEntity(userId);

        // 🔹 events already requested/joined
        List<Long> joinedEventIds = eventVolunteerRepo
                .findByVolunteer(volunteer)
                .stream()
                .map(ev -> ev.getEvent().getId())
                .toList();

        List<Event> events = eventRepo.findAll();

        // 🔹 exclude joined events
        List<Event> availableEvents = events.stream()
                .filter(e -> !joinedEventIds.contains(e.getId()))
                .toList();

        LocalDate today = LocalDate.now();

        return switch (filter.toUpperCase()) {

            case "LIVE" -> availableEvents.stream()
                    .filter(e ->
                            !today.isBefore(e.getStartDate()) &&
                                    !today.isAfter(e.getEndDate())
                    )
                    .toList();

            case "UPCOMING" -> availableEvents.stream()
                    .filter(e -> today.isBefore(e.getStartDate()))
                    .toList();

            case "COMPLETED" -> availableEvents.stream()
                    .filter(e -> today.isAfter(e.getEndDate()))
                    .toList();

            default -> availableEvents;
        };
    }



    @Override
    public List<VolunteerEventStatusDTO> getMyEventsWithStatus(Long userId) {

        Volunteerprofile volunteer = getVolunteerEntity(userId);

        return eventVolunteerRepo.findByVolunteer(volunteer)
                .stream()
                .map(ev -> {
                    Event event = ev.getEvent();

                    VolunteerEventStatusDTO dto = new VolunteerEventStatusDTO();

                    dto.setEventId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setCity(event.getCity());
                    dto.setArea(event.getArea());
                    dto.setStartDate(event.getStartDate());
                    dto.setEndDate(event.getEndDate());

                    dto.setStatus(ev.getStatus());

                    // Only compute attendance/eligibility for APPROVED events
                    if ("APPROVED".equalsIgnoreCase(ev.getStatus())) {
                        // Reuse existing eligibility logic (75% threshold)
                        EligibilityResultDTO eligibility =
                                eligibilityService.checkEligibility(
                                        event.getId(),
                                        volunteer.getId()
                                );

                        boolean certIssued =
                                certificateRepo.existsByEventAndVolunteer(event, volunteer);

                        boolean feedbackGiven =
                                feedbackRepo.existsByEventAndVolunteer(event, volunteer);

                        dto.setAttendancePercentage(eligibility.getAttendancePercentage());
                        dto.setEligible(eligibility.isEligible());
                        dto.setCertificateIssued(certIssued);
                        dto.setFeedbackGiven(feedbackGiven);
                    }

                    return dto;
                })
                .toList();
    }


    @Override
    public void joinEvent(Long eventId, Long userId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Volunteerprofile volunteer = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Volunteer profile not found"));

        // ❌ check via EventVolunteer (NOT event.getVolunteers)
        eventVolunteerRepo.findByEventAndVolunteer(event, volunteer)
                .ifPresent(ev -> {
                    throw new RuntimeException("Already requested");
                });

        EventVolunteer ev = new EventVolunteer();
        ev.setEvent(event);
        ev.setVolunteer(volunteer);
        ev.setStatus("PENDING");
        ev.setRequestedAt(LocalDateTime.now());
        if (LocalDate.now().isAfter(event.getRegisterDeadline())) {
            throw new RuntimeException("Registration deadline has passed");
        }

        eventVolunteerRepo.save(ev);
        String html = """
<div style="font-family: Arial, sans-serif; padding: 20px;">
  <h2 style="color:#0f172a;">Volunteer Request Submitted</h2>

  <p>Hello <b>%s</b>,</p>

  <p>Your request to join the event
     <b>%s</b> has been sent successfully.</p>

  <p>Status:
     <span style="color:#d97706; font-weight:bold;">PENDING</span>
  </p>

  <p>You will be notified once the organizer reviews your request.</p>

  <br/>
  <p style="font-size:12px;color:#64748b;">
    — Voluntub Team
  </p>
</div>
""".formatted(
                volunteer.getUser().getName(),
                event.getTitle()
        );

        emailService.sendHtmlEmail(
                volunteer.getUser().getEmail(),
                "Volunteer Request Submitted",
                html
        );

    }
    @Override
    public VolunteerProfileDTO getProfile(Long userId) {

        Volunteerprofile profile = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        VolunteerProfileDTO dto = new VolunteerProfileDTO();
        dto.setName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setPhone(profile.getPhone());
        dto.setAge(profile.getAge());
        dto.setGender(profile.getGender());
        dto.setOccupation(profile.getOccupation());
        dto.setSkills(profile.getSkills());
        dto.setAvailability(profile.getAvailability());
        dto.setBio(profile.getBio());
        dto.setAddress(profile.getAddress());

        return dto;
    }

    @Override
    public VolunteerProfileDTO updateProfile(Long userId, VolunteerProfileDTO dto) {

        Volunteerprofile profile = volunteerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPhone(dto.getPhone());
        profile.setAge(dto.getAge());
        profile.setGender(dto.getGender());
        profile.setOccupation(dto.getOccupation());
        profile.setSkills(dto.getSkills());
        profile.setAvailability(dto.getAvailability());
        profile.setBio(dto.getBio());
        profile.setAddress(dto.getAddress());

        volunteerRepo.save(profile);

        return getProfile(userId);
    }


}
