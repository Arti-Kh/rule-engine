package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.Rules;
import com.debricked.rule.engine.model.ScanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    @Value("${user.mail.address}")
    private String userMailAddress;

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(ScanResponse result) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userMailAddress);
        email.setSubject("Vulnerabilties Report for your File Upload");
        email.setText(buildMessage(result));
        emailSender.send(email);
    }

    private String buildMessage(ScanResponse result) {
        StringBuilder message = new StringBuilder("Your File Upload Status is")
                .append(result.getProgress())
                .append(". ")
                .append("Vulnerabilities Found - ")
                .append(result.getVulnerabilityCount());
        return message.toString();
    }

}
