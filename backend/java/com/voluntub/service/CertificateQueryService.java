package com.voluntub.service;

import com.voluntub.dto.VolunteerCertificateDTO;
import java.util.List;

public interface CertificateQueryService {

    List<VolunteerCertificateDTO> getMyCertificates(Long userId);
}
