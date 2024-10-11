package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.DependencyFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service performs following operation :
 *  1. Invoke Debricked Authentication APi to get the bearer token.
 *  2. Use the bearer token to upload the file for scan
 *  3. Set the upload id in reporting service where API is invoked to get status every 10 mins.
 */
@Service
public class DebrickedScanService {

    @Value("${debricked.username}")
    private String username;

    @Value("${debricked.password}")
    private String password;

    @Value("${debricked.token.api.url}")
    private String tokenUrl;

    @Value("${debricked.scan.api.url}")
    private String scanUrl;

    private final RestTemplate restTemplate;
    private final ReportScheduler reportScheduler;

    @Autowired
    public DebrickedScanService(RestTemplate restTemplate, ReportScheduler reportScheduler) {
        this.restTemplate = restTemplate;
        this.reportScheduler = reportScheduler;
    }

    public void sendFileForScan(DependencyFile file) {
        // Step 1 : Generate token
        String token = authenticateAndGetToken();

        // Step 2 : Invoke api to scan the file
        ResponseEntity<String> response = uploadFileForScan(token, file);

        // Step 3 : Set the upload id and pass it to reporting service
        reportScheduler.addUploadId(file.getId());
    }

    public String authenticateAndGetToken() {
        // Prepare the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("_username", username);
        requestBody.put("_password", password);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the entity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

        if(response != null) {
            return extractToken(response.getBody());
        }
        // Extract Bearer token from the response body
        return null;
    }

    private String extractToken(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to receive bearer token", e);
        }
    }

    public ResponseEntity<String> uploadFileForScan(String token, DependencyFile file) {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token);

        // Create the request entity with the file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("repositoryZip", file.getFile());
        body.add("ciUploadId", file.getId());

        // Create the request entity with the multipart body
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        // Make the POST request
        return restTemplate.exchange(scanUrl, HttpMethod.POST, requestEntity, String.class);
    }
}
