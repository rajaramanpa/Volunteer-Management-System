package com.voluntub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_feedback",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "volunteer_id"})
        }
)
public class EventFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteerprofile volunteer;

    private int rating; // 1–5
    @Column(length = 1000)
    private String comment;

    private LocalDateTime submittedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }


    // getters setters
}
