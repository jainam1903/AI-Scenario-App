package com.backend_ai_scenario.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend_ai_scenario.model.ScenarioAnalysisRequest;
import com.backend_ai_scenario.model.ScenarioAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String openaiApiUrl;
    
    @Value("${openai.model:gpt-4o-mini}")
    private String model;
    
    @Value("${openai.temperature:0.7}")
    private double temperature;
    
    public ScenarioAnalysisResponse generateAnalysis(ScenarioAnalysisRequest request) throws Exception {
        String prompt = buildPrompt(request);
        String apiResponse = callOpenAiApi(prompt);
        return parseApiResponse(apiResponse);
    }
    
    private String buildPrompt(ScenarioAnalysisRequest request) {
        return "Analyze this scenario and constraints. Respond with ONLY a valid JSON object using this EXACT format:\n"
             + "{\n"
             + "  \"scenarioSummary\": \"One sentence summary\",\n"
             + "  \"potentialPitfalls\": [\"Pitfall 1\", \"Pitfall 2\", \"Pitfall 3\"],\n"
             + "  \"proposedStrategies\": [\"Strategy 1\", \"Strategy 2\", \"Strategy 3\"],\n"
             + "  \"recommendedResources\": [\"Resource 1\", \"Resource 2\", \"Resource 3\"],\n"
             + "  \"disclaimer\": \"Brief disclaimer\"\n"
             + "}\n\n"
             + "Scenario: " + request.getScenario() + "\n"
             + "Constraints: " + String.join(", ", request.getConstraints());
    }
    
    private String callOpenAiApi(String prompt) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);
            
            String requestBody = "{"
                + "\"model\": \"" + model + "\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"You are a helpful AI assistant that analyzes scenarios and provides structured advice.\"},"
                + "  {\"role\": \"user\", \"content\": \"" + escapeJsonString(prompt) + "\"}"
                + "],"
                + "\"temperature\": " + temperature
                + "}";
            
            log.debug("OpenAI Request: {}", requestBody);
            
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            
            // Try up to 3 times with backoff
            Exception lastException = null;
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    if (attempt > 0) {
                        // Wait before retry (exponential backoff)
                        Thread.sleep(1000 * (1 << attempt)); // 1s, 2s, 4s
                    }
                    
                    String response = restTemplate.postForObject(openaiApiUrl, request, String.class);
                    log.debug("OpenAI Response: {}", response);
                    
                    JsonNode rootNode = objectMapper.readTree(response);
                    return rootNode.path("choices").get(0).path("message").path("content").asText();
                } catch (Exception e) {
                    log.warn("Error on attempt {}: {}", attempt + 1, e.getMessage());
                    lastException = e;
                    
                    // If not a rate limit error, don't retry
                    if (!e.getMessage().contains("429") && !e.getMessage().contains("Too Many Requests")) {
                        throw e;
                    }
                }
            }
            
            // If we got here, all attempts failed
            throw lastException;
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            throw e;
        }
    }
    
    private ScenarioAnalysisResponse parseApiResponse(String apiResponse) throws Exception {
        log.debug("Attempting to parse API response: {}", apiResponse);
        
        try {
            // First try to parse as direct JSON
            return objectMapper.readValue(apiResponse, ScenarioAnalysisResponse.class);
        } catch (Exception e) {
            log.info("Could not parse response as direct JSON: {}", e.getMessage());
            
            // Look for JSON pattern in the text response
            int jsonStart = apiResponse.indexOf('{');
            int jsonEnd = apiResponse.lastIndexOf('}') + 1;
            
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                String jsonPart = apiResponse.substring(jsonStart, jsonEnd);
                log.debug("Extracted JSON part: {}", jsonPart);
                return objectMapper.readValue(jsonPart, ScenarioAnalysisResponse.class);
            }
            
            // If we can't parse the response at all, re-throw the exception
            throw new RuntimeException("Could not parse OpenAI response: " + apiResponse);
        }
    }
    
    private String escapeJsonString(String input) {
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
} 