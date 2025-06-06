# AI Scenario Analysis Application

This is a full-stack web application that lets users enter a scenario and constraints, and get AI-powered analysis including scenario summary, potential pitfalls, proposed strategies, recommended resources, and a disclaimer.

## Project Structure

The application consists of two main parts:

1. **Backend**: A Spring Boot application that provides an API endpoint to process scenario analysis requests.
2. **Frontend**: A React application that provides a user interface for entering scenarios and displaying analysis results.

## Setup Instructions

### Prerequisites

- Java 21
- Node.js (v18 or higher)
- npm (v9 or higher)

### Backend Setup

1. Navigate to the backend directory:
   ```
   cd backend-ai-scenario
   ```

2. Update the OpenAI API key:
   - Open `src/main/resources/application.properties`
   - Replace `your_api_key_here` with your actual OpenAI API key

3. Start the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```
   The backend will start on port 8080.

### Frontend Setup

1. Navigate to the frontend directory:
   ```
   cd frontend-ai-scenario
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the React application:
   ```
   npm start
   ```
   The frontend will start on port 3000.

4. Open your browser and navigate to `http://localhost:3000` to use the application.

## API Endpoints

- `GET /api/test` - Test if the API is working
- `GET /api/test-openai` - Test if the OpenAI integration is working
- `POST /api/analyze-scenario` - Analyze a scenario based on the provided description and constraints

Request payload:
```json
{
  "scenario": "Our team has a new client project with a tight deadline and limited budget.",
  "constraints": [
    "Budget: $10,000",
    "Deadline: 6 weeks",
    "Team of 3 developers"
  ]
}
```

Response payload:
```json
{
  "scenarioSummary": "A small team must deliver a client project within 6 weeks on a $10,000 budget.",
  "potentialPitfalls": [
    "Scope creep due to unclear requirements",
    "Underestimation of resource constraints",
    "Risk of burnout with limited manpower"
  ],
  "proposedStrategies": [
    "Define clear milestones and requirements early",
    "Implement lean project management principles",
    "Conduct weekly check-ins to monitor progress"
  ],
  "recommendedResources": [
    "Trello or Jira for agile task management",
    "Open-source libraries to reduce cost",
    "Online tutorials for rapid skill upskilling"
  ],
  "disclaimer": "These suggestions are generated by AI; consult subject matter experts for tailored guidance."
}
```

## Technologies Used

- **Backend**: Spring Boot, OpenAI API
- **Frontend**: React, Tailwind CSS, Axios 