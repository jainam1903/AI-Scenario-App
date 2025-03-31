package com.backend_ai_scenario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.backend_ai_scenario", "com.backend_ai_scenario.controller", "com.backend_ai_scenario.service", "com.backend_ai_scenario.model"})
public class BackendAiScenarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendAiScenarioApplication.class, args);
	}

}
