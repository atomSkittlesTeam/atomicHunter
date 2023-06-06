package net.vniia.skittles.integration;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

@Service
public class ResumeDownloadService {

    private static final String URL_FOR_DOWNLOAD_RESUME = "https://www.africau.edu/images/default/sample.pdf";

    public static final String DOWNLOAD_FILE_PATH = "resumes/";

    private final RestTemplate restTemplate = new RestTemplate();

    public void downloadFileFromUrl() throws IOException {

        String fileName = "resume.pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(URL_FOR_DOWNLOAD_RESUME, HttpMethod.GET, entity, byte[].class);
        this.createFolder();
        Files.write(Paths.get(DOWNLOAD_FILE_PATH + fileName), response.getBody());
    }

    private void createFolder() {
        File directory = new File(DOWNLOAD_FILE_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
