package com.voluntub.dto;

import java.time.LocalDateTime;

public class TicketResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
    private String userName;
    private String userEmail;
    private String role;

    public TicketResponseDTO(
            Long id,
            String title,
            String description,
            String priority,
            String status,
            LocalDateTime createdAt,
            String userName,
            String userEmail,
            String role
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
    }

    // getters only (immutable DTO)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getRole() { return role; }
}
