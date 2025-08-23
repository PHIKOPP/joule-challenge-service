# joule-challenge-service

A simple Spring Boot service for defining digital assistants with fixed response messages. Supports creating named assistants and sending them messages via HTTP API. Built as a coding challenge solution

docker rm -f dasvc
docker build -t digital-assistant-service:local .
docker run -p 8080:8080 --name dasvc digital-assistant-service:local

---

---

# How to start (Docker & Local)

## 🐳 Docker

### Prerequisites

- Docker Engine check with `docker --version`
- Docker Engine running
- Open Terminal in root of project

### Build & Run

```bash
docker build -t joule-challenge-service .
docker run -p 8080:8080 --name joule-service joule-challenge-service
```

### Stop & Remove

```bash
docker stop joule-service
docker rm joule-service
```

## Local with Maven

### Prerequisites

- Java **21** installed and on your PATH check with `java -version`
- If not installed open terminal and execute:
  - ` Set-ExecutionPolicy RemoteSigned -Scope CurrentUser`
  - ` irm get.scoop.sh | iex`
  - ` scoop bucket add java`
  - ` scoop install openjdk21`
  - `java -version`
- Start project with `./mvnw spring-boot:run`

---

---

# API Overview

### 1. Create Assistant

**POST** `/api/v1/assistant`  
Create a new assistant with a name and a predefined response.

**Request**

```json
{
  "name": "joule",
  "response": "Hello, I am Joule!"
}
```

**Response** `201 Created`

```json
{
  "assistant": "joule",
  "response": "Hello, I am Joule!"
}
```

### 2. Get Assistant

**GET** `/api/v1/assistant/{name}`  
Get an existing assistant by name (case-insensitive).

**Response** `200 OK`

```json
{
  "assistant": "joule",
  "response": "Hello, I am Joule!"
}
```

### 3. Send Message

**POST** `/api/v1/assistant/{name}/message`  
Send a message to the assistant and get the response.

**Request**

```json
{ "message": "Hi Joule" }
```

**Response** `200 OK`

```json
{
  "assistant": "joule",
  "response": "Hello, I am Joule!"
}
```

---

---

# Example for testing

### 1. Creating 2 Assistants

**Joule**
`curl -X POST "http://localhost:8080/api/v1/assistant" -H "Content-Type: application/json" -d "{ \"name\": \"joule\", \"response\": \"Hello, I am Joule!\" }"`

**Philipp**
`curl -X POST "http://localhost:8080/api/v1/assistant" -H "Content-Type: application/json" -d "{ \"name\": \"philipp\", \"response\": \"Hello, I am Philipp!\" }"`

---

### 2. Check assistants exist

**Joule**
`curl -X GET "http://localhost:8080/api/v1/assistant/joule"`

**Philipp**
`curl -X GET "http://localhost:8080/api/v1/assistant/Philipp"`

---

### 3. Send messages

**Joule**
`curl -X POST "http://localhost:8080/api/v1/assistant/joule/message" -H "Content-Type: application/json" -d "{ \"message\": \"Hi Joule\" }"`

**Philipp**
`curl -X POST "http://localhost:8080/api/v1/assistant/philipp/message" -H "Content-Type: application/json" -d "{ \"message\": \"Hi Phil\" }"`

---

---

# Design Decisions

1. Technology Stack
   - Spring Boot 3: fast setup, built-in REST support, excellent testability.
   - Java 21: modern, long-term support release.
   - Maven: guarantees reproducible builds without requiring Maven installation.
   - Docker: ensures platform-independent deployment and easy reproducibility.
2. Architecture
   - **Controller** → handles requests and responses
   - **Service** → business logic, validation, case-insensitive lookup
   - **Model** → Assistant class for data representation
3. Why no database?
   - The challenge is scoped to 2–4 hours → focus on API design
   - In-Memory Store is simple and sufficient.

# Potential Extensions

- Create gui in webbrowser
- Better error handling
- Swagger Documentation
- Features:
  - User can update/change response text of assistant
  - User can delete assistant
