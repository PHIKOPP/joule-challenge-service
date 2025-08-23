package pkopp.digital_assistant_service;

public class Assistant {
    private String name;
    private String response;

    public Assistant(String name) {
        this.name = name;
    }

    // Optionally, add getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResponse(String reseponse) {
        this.response = reseponse;
    }

    public String getResponse() {
        return response;
    }

    public String respondTo(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        // Here would implement the logic to process and interact the message
        // Not needed for challenge --> just return with response
        return response;
    }
}
