package com.debricked.rule.engine.service;

import com.debricked.rule.engine.model.RuleAction;
import com.debricked.rule.engine.model.RuleType;
import com.debricked.rule.engine.model.Rules;
import com.debricked.rule.engine.model.ScanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that loads all rules and apply each on the scanned result of uploaded file
 */
@Service
public class RuleService {

    @Autowired
    private List<Rules> rules;

    @Autowired
    private EmailService emailService;

    public List<Rules> getRules() {
        return rules;
    }

    /**
     * Invoke rules for each uploaded file based on the scanned result and apply action
     * @param uploadId
     * @param result
     */
    public void invokeRule(String uploadId, ScanResponse result) {
        for (Rules rule : rules) {
            if (rule.ruleType() == RuleType.UPLOAD_FAILED && result.getProgress().equals("FAILED")) {
                applyAction(rule, uploadId, result);
            } else if (rule.ruleType() == RuleType.UPLOAD_IN_PROGRESS && result.getProgress().equals("IN_PROGRESS")) {
                applyAction(rule, uploadId, result);
            } else if (rule.ruleType() == RuleType.VULNERABILITIES_FOUND && result.getVulnerabilityCount() > rule.threshold()) {
                applyAction(rule, uploadId, result);
            }
        }
    }

    private void applyAction(Rules rule, String uploadId, ScanResponse result) {
        if (rule.ruleAction() == RuleAction.SEND_EMAIL) {
            emailService.sendEmail(result);
        }
    }
}
