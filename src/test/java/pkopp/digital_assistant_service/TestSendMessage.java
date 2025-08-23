package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestSendMessage {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    private final AssistantService service = new AssistantService();

    private void createAssistantViaApi(String name, String response) throws Exception {
        String body = """
                { "name": "%s", "response": "%s" }
                """.formatted(name, response);

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }
    // ----------------------------------------------
    // Testing local Controller
    // ----------------------------------------------

    // good message
    @Test
    void sendMessage_withValidInput_returnsResponse() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String userMessage = "Hello, I am Philipp.";
        String response = a.respondTo(userMessage);
        assertEquals(response, "Hello, I am Joule!");
    }

    // long message
    @Test
    void sendMessage_withLongInput_returnsResponse() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String userMessage = "Hi".repeat(5000);
        String response = a.respondTo(userMessage);
        assertEquals(response, "Hello, I am Joule!");
    }

    // null message
    @Test
    void sendMessage_withNullInput_returnsResponse() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String userMessage = null;
        assertThrows(IllegalArgumentException.class, () -> a.respondTo(userMessage));
    }

    // empty message
    @Test
    void sendMessage_withEmptyInput_returnsResponse() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String userMessage = " ";
        assertThrows(IllegalArgumentException.class, () -> a.respondTo(userMessage));
    }

    // ----------------------------------------------
    // Testing API
    // ----------------------------------------------

    // good message
    @Test
    void sendMessage_existingAssistant_returns200_andResponse() throws Exception {
        createAssistantViaApi("joule", "Hello, I am Joule!");

        // 2. Nachricht senden
        String messageBody = """
                { "message": "Hi Joule" }
                """;

        mockMvc.perform(post("/api/v1/assistant/joule/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, I am Joule!"));
    }

    // long message
    @Test
    void sendMessage_withLongInput_returns200_andResponse() throws Exception {
        createAssistantViaApi("joule", "Hello, I am Joule!");
        String userMessage = "Hi".repeat(5000);
        // 2. Nachricht senden
        String messageBody = """
                { "message": "%s" }
                """.formatted(userMessage);

        mockMvc.perform(post("/api/v1/assistant/joule/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, I am Joule!"));
    }

    // null message
    @Test
    void sendMessage_withNullInput_returns400() throws Exception {
        createAssistantViaApi("joule", "Hello, I am Joule!");

        String messageBody = """
                { "message": null }
                """;

        mockMvc.perform(post("/api/v1/assistant/joule/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageBody))
                .andExpect(status().isBadRequest());
    }

    // empty message
    @Test
    void sendMessage_withEmptyInput_returns400() throws Exception {
        createAssistantViaApi("joule", "Hello, I am Joule!");

        String messageBody = """
                { "message": " " }
                """;

        mockMvc.perform(post("/api/v1/assistant/joule/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageBody))
                .andExpect(status().isBadRequest());
    }

    // no assistant found with that name
    @Test
    void sendMessage_withNoAssistant_returns404() throws Exception {
        mockMvc.perform(post("/api/v1/assistant/unknown/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\":\"Hi\"}"))
                .andExpect(status().isNotFound());
    }
}
