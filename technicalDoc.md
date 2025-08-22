# Technical Design Document: Simple Blog System

**Version:** 1.0  
**Date:** August 22, 2025  
**Author:** Shivam Kumar 

***

## 1. Overview

This document outlines the technical design for a **Simple Blog System**. The project's goal is to create a robust, scalable, and maintainable web application where users can:

- Create posts
- Add comments
- Receive notifications

The system will also track basic analytics for post views.  
The architecture emphasizes **SOLID principles**, design patterns, a decoupled event-driven approach (via message brokers), and observability using **Prometheus** and **Grafana**.

***

## 2. Core Requirements

### 2.1. Functional Requirements (FR)

- **FR1: User Management**
    - Users can register with username, email, and password.
    - Users can log in to obtain an authentication token (e.g., JWT).
    - System validates user input (email format, password strength).

- **FR2: Post Management**
    - Authenticated users can create posts (title + content).
    - Anyone can view all posts.
    - Anyone can view a post by its ID.
    - Only the author can update or delete their post.

- **FR3: Comment Management**
    - Authenticated users can add comments to posts.
    - Anyone can see all comments on a post.
    - Only the comment's author can delete it.

- **FR4: Notifications**
    - When a comment is created, the post's author receives a notification (simulated as a log via a dedicated listener).

- **FR5: Analytics**
    - Each post view triggers an event.
    - A separate process tracks analytics (post view counts).

***

### 2.2. Non-Functional Requirements (NFR)

- **NFR1: Scalability** — Handles growth in users/posts without performance loss.
- **NFR2: Reliability** — Failure in one component (e.g., notifications) doesn't take down the app.
- **NFR3: Maintainability** — Code is clean, documented, and SOLID-compliant.
- **NFR4: Observability** — Key metrics (health, latency, business stats) are exportable for monitoring.
- **NFR5: Security** — Auth required to create/change data; passwords are securely hashed.

***

## 3. System Architecture (High-Level Design)

The system comprises a Spring Boot application (`blog-service`) for business logic, persistent storage in PostgreSQL, and two message brokers (RabbitMQ and Kafka) for async workflows.

**Components:**
- **User/Client:** Consumes the REST API.
- **Blog Service (Spring Boot):** Provides REST API, manages main logic/entities, and acts as event publisher.
- **PostgreSQL:** Main database.
- **RabbitMQ:** Handles notifications (message when new comment added).
- **Kafka:** Handles analytics (message on post view).
- **Prometheus:** Monitors/scrapes metrics from the application.
- **Grafana:** Visualizes metrics from Prometheus.

***

### High-Level Diagram
<img width="3840" height="1550" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090147" src="https://github.com/user-attachments/assets/177c9235-ef60-44ce-9f6a-e0ef8fe54a1e" />

***

## 4. Data Model (Entity-Relationship Diagram)

Three main tables/entities:

- **users:** User account info.
- **posts:** Blog posts (references `user` as author).
- **comments:** Comments on posts (references both `user` and `post`).

<img width="2387" height="3840" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090223" src="https://github.com/user-attachments/assets/54c99da1-3696-470b-8b5a-0c347f9056af" />
***

## 5. Low-Level Design (LLD)

### 5.1. Project Structure (Maven)

```plaintext
simple-blog-system/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/example/blog/
        │       ├── config/          // Spring configs (Security, RabbitMQ, Kafka)
        │       ├── controller/      // REST controllers (UserController, PostController)
        │       ├── dto/             // DTOs (UserDto, PostRequest, etc.)
        │       ├── exception/       // Custom exception classes and global handler
        │       ├── model/           // JPA Entities (User, Post, Comment)
        │       ├── repository/      // Spring Data repositories
        │       ├── security/        // JWT, UserDetailsService, Security configs
        │       ├── service/         // Service layer interfaces
        │       │   └── impl/        // Implementations
        │       └── messaging/       // Rabbit/Kafka producers and consumers
        │           ├── consumer/
        │           └── producer/
        └── resources/
            ├── application.yml      // Main config
            ├── static/
            └── templates/
```

***

### 5.2. Class Diagram (UML)
<img width="3840" height="1249" alt="Untitled diagram _ Mermaid Chart-2025-08-22-090247" src="https://github.com/user-attachments/assets/79fb1c33-d649-4d74-8966-80d006119184" />

***

### 5.3. Design Patterns & Principles

- **SOLID:**
    - *Single Responsibility:* One purpose per class (controller, service, repo)
    - *Open/Closed:* Add listeners/handlers w/o changing service code
    - *Liskov Substitution:* Service impls can replace interfaces
    - *Interface Segregation:* Specific interfaces (PostService, UserService)
    - *Dependency Inversion:* Controllers depend on abstractions only

- **Design Patterns:**
    - *DTO:* Do not expose JPA entities via API
    - *Repository:* Spring Data JPA for DB abstraction
    - *Service Layer:* Encapsulates business logic
    - *Observer (via MQ):* PostService is publisher; listeners are subscribers
    - *Builder:* For constructing complex entities/DTOs

***

## 6. API Design (REST Endpoints)

**Base URL:** `http://localhost:8080`

### User & Auth API

- **POST** `/api/auth/register`  
  Description: Registers a new user  
  Body:
  ```json
  { "username": "newuser", "email": "user@example.com", "password": "strongpassword" }
  ```
  Response:
  ```json
  { "message": "User registered successfully" }
  ```

- **POST** `/api/auth/login`  
  Description: Authenticates and returns JWT  
  Body:
  ```json
  { "username": "newuser", "password": "strongpassword" }
  ```
  Response:
  ```json
  { "token": "..." }
  ```

### Post API

- **POST** `/api/posts`  
  Create post (auth required)  
  Body:
  ```json
  { "title": "My First Post", "content": "This is the content." }
  ```
  Response:
  ```json
  { "id": 1, "title": "My First Post", ... }
  ```

- **GET** `/api/posts`  
  List all posts  
  Response:
  ```json
  [ { "id": 1, "title": "...", ... }, ... ]
  ```

- **GET** `/api/posts/{postId}`  
  Get post by ID. Fires analytics event.  
  Response:
  ```json
  { "id": 1, "title": "...", "content": "...", "authorUsername": "...", "comments": [...] }
  ```

- **PUT** `/api/posts/{postId}`  
  Update post (auth + ownership)  
  Body:
  ```json
  { "title": "Updated Title", "content": "Updated content." }
  ```
  Response:
  ```json
  { "id": 1, "title": "Updated Title", ... }
  ```

- **DELETE** `/api/posts/{postId}`  
  Delete post (auth + ownership)  
  Response:
  ```
  HTTP 204 No Content
  ```

### Comment API

- **POST** `/api/posts/{postId}/comments`  
  Add comment (auth, triggers notification)  
  Body:
  ```json
  { "content": "Great post!" }
  ```
  Response:
  ```json
  { "id": 101, "content": "Great post!", "authorUsername": "commenter" }
  ```

- **DELETE** `/api/comments/{commentId}`  
  Delete comment (auth + ownership)  
  Response:
  ```
  HTTP 204 No Content
  ```

***

## 7. Technology Stack & Tools

| Technology             | Role in Project                                      |
|------------------------|------------------------------------------------------|
| Java 17+               | Core programming language                            |
| Spring Boot 3.x        | Main framework                                       |
| Spring Data JPA        | ORM/data access                                      |
| Spring Security        | Auth/authz handling                                  |
| PostgreSQL             | Relational DB                                        |
| Maven                  | Build tool                                           |
| RabbitMQ               | Message broker for notifications                     |
| Apache Kafka           | Streaming/events for analytics                       |
| Spring for RabbitMQ    | RabbitMQ integration                                 |
| Spring for Kafka       | Kafka integration                                    |
| Prometheus             | Metrics, monitoring                                  |
| Micrometer             | App instrumentation (Spring Boot Actuator)           |
| Grafana                | Metrics visualization and dashboards                 |
| Docker & Docker Compose| Containerized dev setup/deployment                   |
| JWT (Java JWT)         | Secure access tokens                                 |
| Lombok                 | Reduces boilerplate for models & DTOs                |
| JUnit 5, Mockito       | Unit + integration testing                           |

***

## 8. Execution Plan

Project will be built in phases:

1. **Environment Setup**
    - Install Java, Maven, Docker, IDE

2. **Core Application Foundation**
    - Initialize Spring project
    - Define JPA entities (User, Post, Comment) and DTOs
    - Set up PostgreSQL connection
    - Create repositories, services, basic REST endpoints

3. **Security Implementation**
    - Integrate Spring Security
    - Registration/login endpoints
    - JWT generation/validation
    - Secure endpoints by role/ownership

4. **Notification Flow with RabbitMQ**
    - Set up RabbitMQ (Docker)
    - Spring config for RabbitMQ
    - Create RabbitMQProducer for comment events
    - Create NotificationListener to process notifications

5. **Analytics Flow with Kafka**
    - Set up Kafka/Zookeeper (Docker)
    - Spring config for Kafka
    - KafkaProducer for post views
    - AnalyticsListener to track events

6. **Monitoring & Observability**
    - Add Spring Boot Actuator + Micrometer
    - Expose `/actuator/prometheus`
    - Prometheus/Docker setup for scraping metrics
    - Grafana setup for dashboards

7. **Finalizing and Packaging**
    - Add tests
    - Add global error handling
    - Generate API docs (e.g., Swagger)
    - Provide `docker-compose.yml` for the whole stack


---
