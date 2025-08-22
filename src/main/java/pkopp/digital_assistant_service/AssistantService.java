package pkopp.digital_assistant_service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class AssistantService {
    private static final Map<String, Assistant> assistants = new ConcurrentHashMap<>();

    public Assistant createAssistant(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        Assistant a = new Assistant(name);
        assistants.put(name, a);
        return a;
    }

    public Assistant getAssistant(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name darf nicht leer sein");
        }
        return assistants.get(name.toLowerCase()); // gibt null zurück, wenn nicht vorhanden
    }

}
