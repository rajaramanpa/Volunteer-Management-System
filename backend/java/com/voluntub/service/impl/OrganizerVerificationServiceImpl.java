package com.voluntub.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.voluntub.dto.OrganizerDocumentDTO;
import com.voluntub.dto.VerificationStatusDTO;
import com.voluntub.entity.OrganizerProfile;
import com.voluntub.entity.OrganizerVerification;
import com.voluntub.entity.Role;
import com.voluntub.entity.User;
import com.voluntub.repository.OrganizerProfileRepo;
import com.voluntub.repository.OrganizerVerificationRepository;
import com.voluntub.repository.UserRepository;
import com.voluntub.service.OrganizerVerificationService;

@Service
public class OrganizerVerificationServiceImpl
        implements OrganizerVerificationService {

    private final OrganizerProfileRepo organizerRepo;
    private final OrganizerVerificationRepository repo;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR =
            "D:/voluntub/uploads/organizer-docs/";

    public OrganizerVerificationServiceImpl(
            OrganizerProfileRepo organizerRepo,
            OrganizerVerificationRepository repo,
            UserRepository userRepository
    ) {
        this.organizerRepo = organizerRepo;
        this.repo = repo;
        this.userRepository = userRepository;
    }

    @Override
    public void uploadDocument(Long organizerId, MultipartFile file) {

        // organizerId here is the USER ID of the organizer.
        // Ensure an OrganizerProfile exists for this user; create one lazily if missing.
        OrganizerProfile organizer =
                organizerRepo.findByUserId(organizerId)
                        .orElseGet(() -> {
                            User user = userRepository.findById(organizerId)
                                    .orElseThrow(() -> new RuntimeException("User not found for organizerId=" + organizerId));

                            if (user.getRole() != Role.ORGANIZER) {
                                throw new RuntimeException("User is not an organizer");
                            }

                            OrganizerProfile profile = new OrganizerProfile();
                            profile.setUser(user);
                            profile.setOrganizationName(user.getName());
                            profile.setDescription(null);
                            return organizerRepo.save(profile);
                        });

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName =
                    organizerId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

            String filePath = UPLOAD_DIR + fileName;

            file.transferTo(new File(filePath));

            // Always create a new verification entry so organizers can upload multiple documents.
            OrganizerVerification verification = new OrganizerVerification();
            verification.setOrganizer(organizer);
            verification.setDocumentName(file.getOriginalFilename());
            verification.setDocumentPath(filePath);
            verification.setStatus(OrganizerVerification.Status.PENDING);
            verification.setUploadedAt(LocalDateTime.now());

            repo.save(verification);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }


    @Override
    public VerificationStatusDTO getMyStatus(Long organizerId) {

        OrganizerProfile organizer =
                organizerRepo.findByUserId(organizerId)
                        .orElseThrow(() -> new RuntimeException("Organizer not found"));

        List<OrganizerVerification> list =
                repo.findAllByOrganizerOrderByUploadedAtDesc(organizer);
        if (list.isEmpty()) {
            throw new RuntimeException("No document uploaded");
        }

        OrganizerVerification v = list.get(0);

        return new VerificationStatusDTO(
                v.getStatus().name(),
                v.getAdminRemark(),
                v.getDocumentName()
        );
    }
    private OrganizerDocumentDTO toDTO(OrganizerVerification v) {

        OrganizerDocumentDTO dto = new OrganizerDocumentDTO();

        dto.setId(v.getId());
        dto.setOrganizerId(v.getOrganizer().getUser().getId());
        dto.setOrganizerName(v.getOrganizer().getUser().getName());
        dto.setOrganizerEmail(v.getOrganizer().getUser().getEmail());

        dto.setFileName(v.getDocumentName());
        dto.setStatus(v.getStatus().name());
        dto.setAdminRemark(v.getAdminRemark());
        dto.setUploadedAt(v.getUploadedAt());

        dto.setFileUrl(
                "/api/admin/verification/file/" + v.getId()
        );

        return dto;
    }

    @Override
    public List<OrganizerDocumentDTO> getAllVerificationDTOs() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }


    @Override
    public void reviewVerification(
            Long verificationId,
            String status,
            String remark
    ) {
        OrganizerVerification v =
                repo.findById(verificationId)
                        .orElseThrow(() -> new RuntimeException("Not found"));

        v.setStatus(OrganizerVerification.Status.valueOf(status));
        v.setAdminRemark(remark);
        v.setReviewedAt(LocalDateTime.now());

        repo.save(v);
    }

    @Override
    public boolean isOrganizerVerified(Long organizerId) {

        OrganizerProfile organizer =
                organizerRepo.findByUserId(organizerId)
                        .orElseThrow();

        return repo.findAllByOrganizer(organizer)
                .stream()
                .anyMatch(v -> v.getStatus() == OrganizerVerification.Status.APPROVED);
    }
    @Override
    public List<OrganizerVerification> getMyDocuments(Long organizerId) {
        OrganizerProfile organizer =
                organizerRepo.findByUserId(organizerId)
                        .orElseThrow();

        return repo.findAllByOrganizer(organizer);
    }
    @Override
    public ResponseEntity<Resource> viewFile(Long id) {

        OrganizerVerification v =
                repo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Document not found"));

        File file = new File(v.getDocumentPath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        String name = v.getDocumentName().toLowerCase();

        if (name.endsWith(".pdf")) mediaType = MediaType.APPLICATION_PDF;
        else if (name.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
        else if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
            mediaType = MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + v.getDocumentName() + "\""
                )
                .body(resource);
    }
}
