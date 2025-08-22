package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CreateAssistantName {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    private final AssistantService service = new AssistantService();

    @Test
    void createAssistant_withValidName_returnsAssistant() {
        Assistant a = service.createAssistant("joule");
        String name = a.getName();
        assertEquals(name, "joule");
    }

    @Test
    void createAssistant_withEmptyName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createAssistant(" "));
    }

    @Test
    void createAssistant_withNullName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createAssistant(null));
    }

    @Test
    void createAssistant_withLongName_works() {
        String longName = "a".repeat(5000);
        Assistant a = service.createAssistant(longName);
        String name = a.getName();
        assertEquals(name.length(), 5000);
    }

    @Test
    void createAssistant_withValidName_returns201() throws Exception {
        String body = """
                { "name": "joule" }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assistant").value("joule"));
    }

    @Test
    void createAssistant_withEmptyName_returns400() throws Exception {
        String body = """
                { "name": " " }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAssistant_withNullName_returns400() throws Exception {
        String body = """
                { "name": null }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

}
