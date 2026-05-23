package com.voluntub.service;
import org.springframework.core.io.Resource;
import com.voluntub.dto.OrganizerDocumentDTO;
import com.voluntub.dto.VerificationStatusDTO;
import com.voluntub.entity.OrganizerVerification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrganizerVerificationService {

    void uploadDocument(Long organizerId, MultipartFile file);

    VerificationStatusDTO getMyStatus(Long organizerId);



    void reviewVerification(
            Long verificationId,
            String status,
            String remark
    );
    public List<OrganizerDocumentDTO> getAllVerificationDTOs();

    boolean isOrganizerVerified(Long organizerId);
    public List<OrganizerVerification> getMyDocuments(Long organizerId);
    ResponseEntity<Resource> viewFile(Long id);
}
