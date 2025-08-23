package pkopp.digital_assistant_service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class AssistantService {
    private static final Map<String, Assistant> assistants = new ConcurrentHashMap<>();

    public Assistant createAssistant(String name, String response) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        name = name.toLowerCase();
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }
        if (response.trim().isEmpty()) {
            throw new IllegalArgumentException("Response cannot be empty");
        }
        if (assistants.containsKey(name)) {
            throw new IllegalStateException("Assistant already exists");
        }
        Assistant a = new Assistant(name);
        a.setResponse(response);
        assistants.put(name, a);
        return a;
    }

    public Assistant getAssistant(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
        return assistants.get(name.toLowerCase());
    }

}
