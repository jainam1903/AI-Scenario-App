package com.backend_ai_scenario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.backend_ai_scenario.model.ScenarioAnalysisRequest;
import com.backend_ai_scenario.model.ScenarioAnalysisResponse;
import com.backend_ai_scenario.service.AiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // For development - restrict in production
@Slf4j
public class ScenarioAnalysisController {
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Value("${openai.api.url}")
    private String openaiApiUrl;
    
    @Value("${openai.model}")
    private String model;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("API is working!");
    }
    
    @GetMapping("/test-openai")
    public ResponseEntity<String> testOpenAi() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);
            
            String requestBody = "{"
                + "\"model\": \"" + model + "\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"You are a helpful assistant that responds with JSON.\"},"
                + "  {\"role\": \"user\", \"content\": \"Respond with this exact JSON: {\\\"message\\\": \\\"Hello World!\\\"}\"}"
                + "],"
                + "\"temperature\": 0.5"
                + "}";
            
            log.debug("OpenAI Test Request: {}", requestBody);
            
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            
            // Try up to 3 times with backoff
            for (int attempt = 0; attempt < 3; attempt++) {
                try {
                    if (attempt > 0) {
                        // Wait before retry
                        Thread.sleep(1000 * (1 << attempt)); // 1s, 2s, 4s
                    }
                    
                    String response = restTemplate.postForObject(openaiApiUrl, request, String.class);
                    
                    log.debug("OpenAI Test Response: {}", response);
                    
                    JsonNode rootNode = objectMapper.readTree(response);
                    String message = rootNode.path("choices").get(0).path("message").path("content").asText();
                    
                    return ResponseEntity.ok("OpenAI API responded with: " + message);
                } catch (Exception e) {
                    log.warn("Error on attempt {}: {}", attempt + 1, e.getMessage());
                    
                    // If not a rate limit error, don't retry
                    if (!e.getMessage().contains("429") && !e.getMessage().contains("Too Many Requests")) {
                        throw e;
                    }
                }
            }
            
            return ResponseEntity.status(500).body("Failed after multiple attempts due to rate limiting");
        } catch (Exception e) {
            log.error("Error testing OpenAI API", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/analyze-scenario")
    public ResponseEntity<ScenarioAnalysisResponse> analyzeScenario(
            @RequestBody ScenarioAnalysisRequest request) {
        
        log.info("Received scenario analysis request: {}", request);
        try {
            ScenarioAnalysisResponse response = aiService.generateAnalysis(request);
            log.info("Generated analysis response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing scenario analysis", e);
            return ResponseEntity.status(500).build();
        }
    }
} 