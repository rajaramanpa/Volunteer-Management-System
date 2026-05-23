package com.voluntub.dto;

public class EligibilityResultDTO {

    private Long volunteerId;
    private double attendancePercentage;
    private boolean eligible;

    public EligibilityResultDTO(
            Long volunteerId,
            double attendancePercentage,
            boolean eligible
    ) {
        this.volunteerId = volunteerId;
        this.attendancePercentage = attendancePercentage;
        this.eligible = eligible;
    }

    public Long getVolunteerId() { return volunteerId; }
    public double getAttendancePercentage() { return attendancePercentage; }
    public boolean isEligible() { return eligible; }
}
