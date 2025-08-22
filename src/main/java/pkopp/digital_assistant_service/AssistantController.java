package pkopp.digital_assistant_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {
    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    public record CreateAssistantRequest(String name) {
    }

    public record AssistantResponse(String assistant) {
    }

    @PostMapping
    public ResponseEntity<AssistantResponse> createAssistant(@RequestBody CreateAssistantRequest request) {
        try {
            Assistant assistant = assistantService.createAssistant(request.name());
            String name = assistant.getName();
            return new ResponseEntity<>(new AssistantResponse(name), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
