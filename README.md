# Cricket Score Management System

A Spring Boot application that simulates a live cricket scoring platform with REST APIs, real-time score updates, and a responsive dashboard.

## Features

- Live cricket match dashboard with multi-match overview
- REST API for fetching match details and summaries
- Real-time updates using Server-Sent Events (SSE)
- Match simulation engine for score progression and player stats
- Responsive UI built with vanilla HTML, CSS, and JavaScript
- In-memory data model suitable for demo and local development

## Tech Stack

- Java 17
- Spring Boot 3.x
- Maven
- HTML/CSS/JavaScript

## Run locally

```bash
./mvnw spring-boot:run
```

Then open:

- http://localhost:8080
- API base: http://localhost:8080/api

## API endpoints

- GET /api/matches
- GET /api/matches/{id}
- GET /api/matches/{id}/summary
- GET /api/live/stream

## Project structure

```text
src/
  main/
    java/com/prashik/cricket/
      controller/
      model/
      service/
      scheduler/
      CricketScoreManagementApplication.java
    resources/
      application.properties
      static/
        index.html
        css/styles.css
        js/app.js
```

## Notes

This project simulates live match activity and is designed to be extended with databases, authentication, and a production-ready frontend framework.
