package com.voluntub.dto;

import java.time.LocalDate;

public class VolunteerCertificateDTO {

    private Long eventId;
    private String eventTitle;
    private LocalDate issuedDate;
    private String certificateUrl;

    public VolunteerCertificateDTO(
            Long eventId,
            String eventTitle,
            LocalDate issuedDate,
            String certificateUrl
    ) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.issuedDate = issuedDate;
        this.certificateUrl = certificateUrl;
    }

    public Long getEventId() { return eventId; }
    public String getEventTitle() { return eventTitle; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public String getCertificateUrl() { return certificateUrl; }
}
