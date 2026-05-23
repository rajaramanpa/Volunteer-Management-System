package com.voluntub.dto;

public class VerificationStatusDTO {

    private String status;
    private String adminRemark;
    private String documentName;

    public VerificationStatusDTO(
            String status,
            String adminRemark,
            String documentName
    ) {
        this.status = status;
        this.adminRemark = adminRemark;
        this.documentName = documentName;
    }

    public String getStatus() { return status; }
    public String getAdminRemark() { return adminRemark; }
    public String getDocumentName() { return documentName; }
}
