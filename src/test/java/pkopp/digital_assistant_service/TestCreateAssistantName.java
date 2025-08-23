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
class TestCreateAssistantName {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    private final AssistantService service = new AssistantService();

    // ----------------------------------------------
    // Testing local Controller
    // ----------------------------------------------

    @Test
    void createAssistant_withValidName_returnsAssistant() {
        Assistant a = service.createAssistant("joule", "Hello, I am Joule!");
        String name = a.getName();
        assertEquals(name, "joule");
    }

    @Test
    void createAssistant_withEmptyName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createAssistant(" ", "Hello, I am Joule!"));
    }

    @Test
    void createAssistant_withNullName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createAssistant(null, "Hello, I am Joule!"));
    }

    @Test
    void createAssistant_withLongName_works() {
        String longName = "a".repeat(5000);
        Assistant a = service.createAssistant(longName, "Hello, I am " + longName + "!");
        String name = a.getName();
        assertEquals(name.length(), 5000);
    }

    // ----------------------------------------------
    // Testing API
    // ----------------------------------------------

    @Test
    void createAssistant_withValidName_returns201() throws Exception {
        String body = """
                { "name": "joule" ,
                "response": "Hello, I am Joule!" }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("joule"));
    }

    @Test
    void createAssistant_withEmptyName_returns400() throws Exception {
        String body = """
                { "name": " " ,
                "response": "Hello, I am Joule!" }
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

    @Test
    void createAssistant_duplicateSameCase_returns409() throws Exception {
        String body = """
                { "name": "Joule" ,
                "response": "Hello, I am Joule!" }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void createAssistant_duplicateCaseInsensitive_returns409() throws Exception {
        String body = """
                { "name": "Joule" ,
                "response": "Hello, I am Joule!" }
                """;
        String body1 = """
                { "name": "joUlE" ,
                "response": "Hello, I am joUlE!" }
                """;

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/assistant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body1))
                .andExpect(status().isConflict());
    }

}
