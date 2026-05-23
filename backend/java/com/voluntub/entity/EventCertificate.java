package com.voluntub.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "event_certificates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "volunteer_id"})
        }
)
public class EventCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteerprofile volunteer;

    private LocalDate issuedDate;

    private String certificateUrl; // later PDF / S3 / local

    // ---- getters setters ----

    public Long getId() { return id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Volunteerprofile getVolunteer() { return volunteer; }
    public void setVolunteer(Volunteerprofile volunteer) { this.volunteer = volunteer; }

    public LocalDate getIssuedDate() { return issuedDate; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }

    public String getCertificateUrl() { return certificateUrl; }
    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }
}
