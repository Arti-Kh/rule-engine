package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.ScanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Value("${user.mail.address}")
    private String userMailAddress;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testSendEmail() {
        // Prepare
        ScanResponse response = new ScanResponse();
        response.setProgress("Completed");
        response.setVulnerabilityCount(5);

        // Action
        emailService.sendEmail(response);

        // Verify
        SimpleMailMessage expectedEmail = new SimpleMailMessage();
        expectedEmail.setTo(userMailAddress);
        expectedEmail.setSubject("Vulnerabilties Report for your File Upload");
        expectedEmail.setText("Your File Upload Status isCompleted. Vulnerabilities Found - 5");

        verify(emailSender, times(1)).send(expectedEmail);
    }
}