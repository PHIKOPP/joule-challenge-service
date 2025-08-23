package pkopp.digital_assistant_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public record CreateAssistantRequest(String name, String response) {
    }

    public record UpdateTextRequest(String response) {
    }

    public record AssistantResponse(String name, String response) {
    }

    public record SendMessageRequest(String message) {
    }

    @PostMapping
    public ResponseEntity<AssistantResponse> createAssistant(@RequestBody CreateAssistantRequest request) {
        try {
            Assistant assistant = assistantService.createAssistant(request.name(), request.response());
            String name = assistant.getName();
            String response = assistant.getResponse();
            return new ResponseEntity<>(new AssistantResponse(name, response), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<AssistantResponse> getAssistant(@PathVariable String name) {
        try {
            Assistant assistant = assistantService.getAssistant(name);
            if (assistant == null) {
                return ResponseEntity.notFound().build(); // 404
            }
            String aName = assistant.getName();
            String response = assistant.getResponse();
            return ResponseEntity.ok(new AssistantResponse(aName, response)); // 200
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @PostMapping("/{name}/message")
    public ResponseEntity<String> sendMessage(@PathVariable String name, @RequestBody SendMessageRequest request) {
        try {
            Assistant assistant = assistantService.getAssistant(name);
            if (assistant == null) {
                return ResponseEntity.notFound().build();
            }
            String response = assistant.respondTo(request.message());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
