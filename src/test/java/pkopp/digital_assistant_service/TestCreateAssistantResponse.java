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
class TestCreateAssistantResponse {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    private final AssistantService service = new AssistantService();

    // Name always the same

    // ----------------------------------------------
    // Testing local Controller
    // ----------------------------------------------

    // 1 good responses
    @Test
    void createAssistant_withValidName_returnsAssistant() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String response = a.getResponse();
        assertEquals(response, "Hello, I am Joule!");
    }

    // 1 empty response
    @Test
    void createAssistant_withEmptyResponse_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.createAssistant("joule", " "));
    }

    // 1 null response
    @Test
    void createAssistant_withNullResponse_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.createAssistant("joule", null));
    }

    // 1 long response
    @Test
    void createAssistant_withLongResponse_works() {
        String longResponse = "Hi".repeat(5000);
        Assistant a = service.createAssistant("joule", longResponse);
        String response = a.getResponse();
        assertEquals(response.length(), 10000); // 10000, cuz "Hi" is 2 chars, repeated 5000 times
    }

    // ----------------------------------------------
    // Testing API
    // ----------------------------------------------

    // good response
    @Test
    void createAssistant_withValidResponse_returns201() throws Exception {
        String body = """
                { "name": "joule" ,
                "response": "Hello, I am Joule!" }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("Hello, I am Joule!"));
    }

    // long response
    @Test
    void createAssistant_withLongResponse_returns201() throws Exception {
        String longResponse = "Hi".repeat(5000);
        String body = """
                { "name": "joule" ,
                "response": "%s" }
                """.formatted(longResponse);

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value(longResponse));
    }

    // empty response
    @Test
    void createAssistant_withEmptyResponse_returns400() throws Exception {
        String body = """
                { "name": "joule" ,
                "response": " " }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    // null response
    @Test
    void createAssistant_withNullResponse_returns400() throws Exception {
        String body = """
                { "name": "joule" ,
                "response": null }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

}
