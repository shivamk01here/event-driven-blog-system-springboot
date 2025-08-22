
# Simple Blog System

A modern, educational blog platform built with **Spring Boot, RabbitMQ, Kafka, Prometheus, and Grafana**. The system demonstrates best practices in software engineering, event-driven architecture, observability, and documentation.

***

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Diagrams](#system-diagrams)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Reference](#api-reference)
- [Observability (Prometheus & Grafana)](#observability)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

***

## Project Overview

- **Users** register/login, create blog posts, and comment on posts.
- **Notifications** are triggered on new comments (delivered via RabbitMQ).
- **Analytics** track post views (using Kafka).
- **Metrics** and **Dashboards** are enabled with Prometheus and Grafana.
- **Built with SOLID, OOP, and well-established patterns—intended as a learning/demo project.**

***

## Architecture

**High-level Architecture:**



**Event-Driven Design:**  
- Blog actions trigger events.
- Asynchronous processing using RabbitMQ and Kafka.

## Features

- **User Management:** Register & login with JWT-secured endpoints.
- **Post CRUD:** Create, read, update, and delete your blog posts.
- **Commenting:** Add and remove comments on posts.
- **Notifications:** Authors get notified when their posts are commented on.
- **Analytics:** Track and view post view counts.
- **RESTful API:** Well-defined endpoints, easily testable via Postman or Swagger.
- **Monitoring:** Prometheus and Grafana integration for system and business metrics.

***

## Technology Stack

| Technology         | Purpose                                       |
|--------------------|-----------------------------------------------|
| Spring Boot 3      | Main application framework                    |
| Spring Data JPA    | ORM for PostgreSQL                            |
| Spring Security    | Authentication/authorization                  |
| RabbitMQ           | Asynchronous notification processing           |
| Kafka              | Event streaming for analytics                 |
| Prometheus         | Metrics scraping                              |
| Grafana            | Metrics dashboards                            |
| Docker Compose     | One-command setup of app and dependencies     |
| PostgreSQL         | Database                                      |
| Micrometer         | Metrics collection (via Spring Boot Actuator) |
| JUnit, Mockito     | Testing                                       |
| Lombok             | Less Java boilerplate                         |

***

## System Diagrams

<img width="3840" height="1550" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090147" src="https://github.com/user-attachments/assets/e613ca20-685e-4bd5-8b54-22622fb7b09b" />


### Entity Relationship

<img width="2387" height="3840" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090223" src="https://github.com/user-attachments/assets/904eb50a-7e35-421c-97ef-8230af0fa136" />


### Class Relationships

<img width="3840" height="1249" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090247" src="https://github.com/user-attachments/assets/3a2a0ed4-1380-4c61-b233-e59cce3c4b1e" />



***

## Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- (Recommended) **Git**, **IntelliJ IDEA** or **VSCode**

### Clone the Repository

```bash
git clone https://github.com/yourusername/simple-blog-system.git
cd simple-blog-system
```

### Build the Application

```bash
mvn clean install
```

### Run Everything with Docker Compose

This command starts the database, RabbitMQ, Kafka, Zookeeper, Prometheus, Grafana, and the blog app:

```bash
docker-compose up --build
```
- By default, the application will be available at `http://localhost:8080`.
- PostgreSQL (localhost:5432), RabbitMQ Management UI (localhost:15672), Prometheus (localhost:9090), Grafana (localhost:3000).

**(Optional) Initialize Database Schema:**  
Spring Boot will auto-create schema on app start using JPA/Hibernate. Edit `application.yml` if you want to tune.

***

## Project Structure

```
simple-blog-system/
├── src/main/java/com/example/blog/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── exception/
│   ├── model/
│   ├── repository/
│   ├── security/
│   ├── service/
│   │   └── impl/
│   └── messaging/
│       ├── producer/
│       └── consumer/
├── src/main/resources/
│   ├── application.yml
│   └── static/
│   └── templates/
├── docker-compose.yml
├── pom.xml
└── README.md
```

***

## API Reference

### Auth

| Method | Endpoint           | Description                       |
|--------|--------------------|-----------------------------------|
| POST   | /api/auth/register | Register new user                 |
| POST   | /api/auth/login    | Get JWT token (login)             |

### Posts

| Method | Endpoint                 | Auth | Description              |
|--------|--------------------------|------|--------------------------|
| POST   | /api/posts               | Yes  | Create new post          |
| GET    | /api/posts               | No   | List all posts           |
| GET    | /api/posts/{postId}      | No   | View specific post       |
| PUT    | /api/posts/{postId}      | Yes  | Edit your post           |
| DELETE | /api/posts/{postId}      | Yes  | Delete your post         |

### Comments

| Method | Endpoint                              | Auth | Description             |
|--------|---------------------------------------|------|-------------------------|
| POST   | /api/posts/{postId}/comments          | Yes  | Comment on post         |
| DELETE | /api/comments/{commentId}             | Yes  | Delete your comment     |

- All POST/PUT endpoints require `Content-Type: application/json` and JWT token in `Authorization: Bearer <token>`.

***

## Observability

### Prometheus

- **Blog service exposes metrics at**:  
  `http://localhost:8080/actuator/prometheus`
- Prometheus is pre-configured in `docker-compose.yml` to scrape metrics from the application.

### Grafana

- **Access Grafana dashboards at:**  
  `http://localhost:3000` (default user: admin / pass: admin)

1. Add Prometheus as a data source (often auto-configured).
2. Import [Sample Dashboard](docs/grafana-dashboard.json) or build your own!

***

## Testing

- **Unit & Integration Tests:** Run with

```bash
mvn test
```

- **Manual API Testing:**  
  Use Postman collection provided or test endpoints via Swagger UI (`/swagger-ui.html`).

***

## Contributing

- Fork the repo, branch with feature/fix name, submit pull requests
- Follow SOLID, clean code, and Java conventions
- Document any design/code decisions

***

## License

MIT (or specify your license)

***

**Diagrams & Visuals:**  
Insert your exported Mermaid/Excalidraw/Whimsical PNGs in the correct locations above for a seamless onboarding and architectural overview.

***

**Questions or help needed?**  
Raise an issue or contact repo maintainers!

***
