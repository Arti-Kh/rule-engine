package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.DependencyFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DebrickedScanServiceTest {

    @InjectMocks
    private DebrickedScanService debrickedScanService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ReportScheduler reportScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        debrickedScanService = new DebrickedScanService(restTemplate, reportScheduler);

    }

    @Test
    public void testSendFileForScan() {
        // Prepare
        DependencyFile file = new DependencyFile("1","test", "in progress", new File(""));

        // Mocking token response
        String token = "mockBearerToken";
        ResponseEntity<String> tokenResponse = ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(tokenResponse);

        // Mocking scan response
        ResponseEntity<String> scanResponse = ResponseEntity.ok("Scan completed");
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class))).thenReturn(scanResponse);

        // Act
        debrickedScanService.sendFileForScan(file);

        // Assert
        verify(reportScheduler, times(1)).addUploadId(file.getId());
    }
}