package com.voluntub.dto;

public class ApprovedVolunteerDTO {

    private Long volunteerId;
    private String name;
    private String email;

    // Optional per-date attendance info
    private String status;          // PRESENT / ABSENT
    private boolean attendanceMarked;

    public ApprovedVolunteerDTO() {
    }

    public ApprovedVolunteerDTO(Long volunteerId, String name, String email) {
        this.volunteerId = volunteerId;
        this.name = name;
        this.email = email;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAttendanceMarked() {
        return attendanceMarked;
    }

    public void setAttendanceMarked(boolean attendanceMarked) {
        this.attendanceMarked = attendanceMarked;
    }
}
