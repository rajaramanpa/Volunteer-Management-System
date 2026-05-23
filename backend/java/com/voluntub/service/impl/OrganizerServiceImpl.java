package com.voluntub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voluntub.dto.VolunteerRequestDTO;
import com.voluntub.entity.Event;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.repository.EventRepository;
import com.voluntub.repository.EventVolunteerRepo;
import com.voluntub.service.EmailService;
import com.voluntub.service.OrganizerService;
import com.voluntub.service.OrganizerVerificationService;

@Service
public class OrganizerServiceImpl implements OrganizerService {
@Autowired
    private EmailService emailService;
    private final EventVolunteerRepo eventVolunteerRepo;
    private final OrganizerVerificationService organizerVerificationService;

    public OrganizerServiceImpl(EventVolunteerRepo eventVolunteerRepo,
                                OrganizerVerificationService organizerVerificationService) {
        this.eventVolunteerRepo = eventVolunteerRepo;
        this.organizerVerificationService = organizerVerificationService;
    }
@Autowired
private  EventRepository eventRepo;

    @Override
    public void approveVolunteer(Long requestId) {

        EventVolunteer ev = eventVolunteerRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Event event = ev.getEvent();

        long approvedCount = eventVolunteerRepo
                .countByEventAndStatus(event, "APPROVED");

        if (approvedCount >= event.getRequiredVolunteers()) {
            throw new RuntimeException("Volunteer limit reached");
        }

        ev.setStatus("APPROVED");
        eventVolunteerRepo.save(ev);
        String html = """
<div style="font-family: Arial; padding:20px;">
  <h2 style="color:#166534;">🎉 Request Approved!</h2>

  <p>Hello <b>%s</b>,</p>

  <p>Your request to join
     <b>%s</b> has been <b>APPROVED</b>.</p>

  <p style="color:#166534;font-weight:bold;">
     You are officially a participant 🎯
  </p>

  <br/>
  <p>See you at the event!</p>
</div>
""".formatted(
                ev.getVolunteer().getUser().getName(),
                ev.getEvent().getTitle()
        );

        emailService.sendHtmlEmail(
                ev.getVolunteer().getUser().getEmail(),
                "Volunteer Request Approved",
                html
        );

    }

    @Override
    public void rejectVolunteer(Long id) {
        EventVolunteer ev = eventVolunteerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        ev.setStatus("REJECTED");

        eventVolunteerRepo.save(ev);
        String html = """
<div style="font-family: Arial; padding:20px;">
  <h2 style="color:#991b1b;">Request Update</h2>

  <p>Hello <b>%s</b>,</p>

  <p>Your request for the event
     <b>%s</b> was not approved.</p>

  <p style="color:#991b1b;">
     Please don’t be discouraged — more events are coming!
  </p>
</div>
""".formatted(
                ev.getVolunteer().getUser().getName(),
                ev.getEvent().getTitle()
        );

        emailService.sendHtmlEmail(
                ev.getVolunteer().getUser().getEmail(),
                "Volunteer Request Update",
                html
        );

    }
    @Override
    public List<VolunteerRequestDTO> getRequestsForEvent(Long eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return eventVolunteerRepo.findByEvent(event)
                .stream()
                .map(ev -> {
                    VolunteerRequestDTO dto = new VolunteerRequestDTO();

                    dto.setRequestId(ev.getId());
                    dto.setStatus(ev.getStatus());
                    dto.setRequestedAt(ev.getRequestedAt());

                    dto.setVolunteerId(ev.getVolunteer().getId());
                    dto.setVolunteerName(ev.getVolunteer().getUser().getName());
                    dto.setVolunteerEmail(ev.getVolunteer().getUser().getEmail());
                    dto.setSkills(ev.getVolunteer().getSkills());
                    dto.setAvailability(ev.getVolunteer().getAvailability());
                    return dto;
                })
                .toList();

    }

    @Override
    public boolean isOrganizerVerified(Long organizerUserId) {
        return organizerVerificationService.isOrganizerVerified(organizerUserId);
    }

}
