package pkopp.digital_assistant_service;

import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    public Assistant createAssistant(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return new Assistant(name);
    }
}
