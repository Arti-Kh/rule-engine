package com.debricked.rule.engine.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Captures the API response received
 */
@Getter
@Setter
public class ScanResponse {
    private String progress;
    private int vulnerabilityCount;

}
