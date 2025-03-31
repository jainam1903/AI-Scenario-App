package com.backend_ai_scenario.model;

import java.util.List;

import lombok.Data;

@Data
public class ScenarioAnalysisRequest {
    private String scenario;
    private List<String> constraints;
} 