package com.voluntub.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.nio.file.Files;

@Service
public class CertificatePdfService {

    public byte[] generateCertificate(
            String volunteerName,
            String eventTitle,
            String date
    ) {
        try {
            String html = Files.readString(
                    new ClassPathResource("templates/certificate.html")
                            .getFile().toPath()
            );

            html = html.replace("{{name}}", volunteerName)
                    .replace("{{event}}", eventTitle)
                    .replace("{{date}}", date);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
