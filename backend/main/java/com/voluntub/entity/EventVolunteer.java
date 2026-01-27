package com.voluntub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_volunteers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "volunteer_id"})
        }
)
public class EventVolunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // 🔗 Volunteer Profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteerprofile volunteer;

    @Column(nullable = false)
    private String status = "PENDING";


    @Column(nullable = true)
    private LocalDateTime requestedAt;

    // -------- ENUM --------


    // -------- GETTERS & SETTERS --------

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Volunteerprofile getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteerprofile volunteer) {
        this.volunteer = volunteer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
