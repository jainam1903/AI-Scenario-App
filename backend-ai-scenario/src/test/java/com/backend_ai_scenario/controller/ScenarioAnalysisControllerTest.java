package com.backend_ai_scenario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.backend_ai_scenario.model.ScenarioAnalysisRequest;
import com.backend_ai_scenario.model.ScenarioAnalysisResponse;
import com.backend_ai_scenario.service.AiService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScenarioAnalysisControllerTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private ScenarioAnalysisController scenarioAnalysisController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scenarioAnalysisController).build();
    }

    @Test
    public void testEndpoint_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("API is working!"));
    }

    @Test
    public void analyzeScenario_WithValidRequest_ShouldReturnAnalysis() throws Exception {
        // Prepare test data
        ScenarioAnalysisRequest request = new ScenarioAnalysisRequest();
        request.setScenario("Test scenario");
        request.setConstraints(Arrays.asList("Constraint 1", "Constraint 2"));

        ScenarioAnalysisResponse response = new ScenarioAnalysisResponse();
        response.setScenarioSummary("Summary of test scenario");
        response.setPotentialPitfalls(Arrays.asList("Pitfall 1", "Pitfall 2"));
        response.setProposedStrategies(Arrays.asList("Strategy 1", "Strategy 2"));
        response.setRecommendedResources(Arrays.asList("Resource 1", "Resource 2"));
        response.setDisclaimer("Test disclaimer");

        // Mock service response
        when(aiService.generateAnalysis(any(ScenarioAnalysisRequest.class))).thenReturn(response);

        // Perform test
        mockMvc.perform(post("/api/analyze-scenario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scenarioSummary").value("Summary of test scenario"))
                .andExpect(jsonPath("$.potentialPitfalls[0]").value("Pitfall 1"))
                .andExpect(jsonPath("$.proposedStrategies[0]").value("Strategy 1"))
                .andExpect(jsonPath("$.recommendedResources[0]").value("Resource 1"))
                .andExpect(jsonPath("$.disclaimer").value("Test disclaimer"));
    }

    @Test
    public void analyzeScenario_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        // Prepare test data
        ScenarioAnalysisRequest request = new ScenarioAnalysisRequest();
        request.setScenario("Test scenario");
        request.setConstraints(Arrays.asList("Constraint 1", "Constraint 2"));

        // Mock service exception
        when(aiService.generateAnalysis(any(ScenarioAnalysisRequest.class))).thenThrow(new RuntimeException("Test exception"));

        // Perform test
        mockMvc.perform(post("/api/analyze-scenario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
} 