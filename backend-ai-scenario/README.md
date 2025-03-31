# AI Scenario Analysis Backend

This is the backend service for the AI Scenario Analysis application that provides an API to analyze scenarios using OpenAI's API.

## Setup

### Prerequisites
- Java 21
- Maven
- OpenAI API Key

### Configuration

#### Setting Up application.properties

The `application.properties` file is not included in the repository for security reasons. To set up your environment:

1. Copy the template file:
```bash
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

2. Set your OpenAI API key as an environment variable:

```bash
# Linux/Mac
export OPENAI_API_KEY=your-api-key-here

# Windows
set OPENAI_API_KEY=your-api-key-here
```

Or edit your `application.properties` file directly to add your API key (NOT recommended for production):

```
openai.api.key=your-api-key-here
```

> **WARNING**: Do not commit your API key to version control.

#### Alternative: Using Profiles

For development purposes, you can create a `application-dev.properties` file in `src/main/resources/` with your API key (make sure this file is in the .gitignore):

```
openai.api.key=your-api-key-here
```

And run the application with the `dev` profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Running the Application

Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api`

## Endpoints

- `GET /api/test` - Test if the API is working
- `GET /api/test-openai` - Test if the OpenAI integration is working
- `POST /api/analyze-scenario` - Analyze a scenario based on the provided description and constraints

## Security Note

Always keep your API keys secure and never commit them to version control. The application is configured to use environment variables for sensitive information. 