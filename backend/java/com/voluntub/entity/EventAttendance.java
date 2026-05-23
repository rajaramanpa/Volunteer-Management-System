package com.voluntub.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "event_attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "volunteer_id", "attendance_date"})
        }
)
public class EventAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // 🔗 Volunteer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteerprofile volunteer;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private String status; // PRESENT / ABSENT

    // ===== Getters & Setters =====

    public Long getId() { return id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Volunteerprofile getVolunteer() { return volunteer; }
    public void setVolunteer(Volunteerprofile volunteer) {
        this.volunteer = volunteer;
    }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
