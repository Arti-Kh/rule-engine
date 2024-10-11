package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.ScanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Scheduler to check the status of file upload
 * and invokes rules based on the response received
 */
@Service
public class ReportScheduler {

    @Value("${debricked.result.api.url}")
    private String url;

    private final RestTemplate restTemplate;
    private final RuleService ruleService;

    private final ConcurrentHashMap<String, String> uploadIds = new ConcurrentHashMap<>();

    @Autowired
    public ReportScheduler(RestTemplate restTemplate, RuleService ruleService) {
        this.restTemplate = restTemplate;
        this.ruleService = ruleService;
    }

    public void addUploadId(String uploadId) {
        uploadIds.put(uploadId, "pending");
    }

    /**
     * Schedule to check vulnerabilities of a given uploaded file.
     * Based on the response redirects to rule service to apply rules.
     */
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void checkVulnerabilities() {
        for (String uploadId : uploadIds.keySet()) {
            String apiUrl = url + "?ciUploadId=" + uploadId;
            ScanResponse result = restTemplate.getForObject(apiUrl, ScanResponse.class);

            // invoke rule
            ruleService.invokeRule(uploadId, result);
            uploadIds.put(uploadId, result.getProgress());
        }
    }
}
