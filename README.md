# joule-challenge-service

A simple Spring Boot service for defining digital assistants with fixed reply messages. Supports creating named assistants and sending them messages via HTTP API. Built as a coding challenge solution

docker rm -f dasvc
docker build -t digital-assistant-service:local .
docker run -p 8080:8080 --name dasvc digital-assistant-service:local

# Create new assitant with name & response

### Joule

curl -X POST "http://localhost:8080/api/v1/assistant" -H "Content-Type: application/json" -d "{ \"name\": \"joule\", \"response\": \"Hello, I am Joule!\" }"

### Philipp

curl -X POST "http://localhost:8080/api/v1/assistant" -H "Content-Type: application/json" -d "{ \"name\": \"philipp\", \"response\": \"Hello, I am Philipp!\" }"

# Send Message

### Joule

curl -X POST "http://localhost:8080/api/v1/assistant/joule/message" -H "Content-Type: application/json" -d "{ \"message\": \"Hi Joule\" }"

### Philipp

curl -X POST "http://localhost:8080/api/v1/assistant/philipp/message" -H "Content-Type: application/json" -d "{ \"message\": \"Hi Joule\" }"

# Change reponsemessage

TODO
