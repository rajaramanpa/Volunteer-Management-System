package com.voluntub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "organizer_verifications"
)
public class OrganizerVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private OrganizerProfile organizer;


    private String documentName;
    private String documentPath;   // local path or cloud url

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private String adminRemark;

    private LocalDateTime uploadedAt;
    private LocalDateTime reviewedAt;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    /* ===== GETTERS & SETTERS ===== */

    public Long getId() { return id; }

    public OrganizerProfile getOrganizer() { return organizer; }
    public void setOrganizer(OrganizerProfile organizer) {
        this.organizer = organizer;
    }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAdminRemark() { return adminRemark; }
    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
