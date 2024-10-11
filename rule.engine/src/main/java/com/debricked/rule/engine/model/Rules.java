package com.debricked.rule.engine.model;

public record Rules(String ruleId, RuleType ruleType, int threshold, RuleAction ruleAction) {
}
