package com.backend_ai_scenario.model;

import java.util.List;

import lombok.Data;

@Data
public class ScenarioAnalysisResponse {
    private String scenarioSummary;
    private List<String> potentialPitfalls;
    private List<String> proposedStrategies;
    private List<String> recommendedResources;
    private String disclaimer;
} 