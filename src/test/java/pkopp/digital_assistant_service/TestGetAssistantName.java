package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestGetAssistantName {

        @Test
        void contextLoads() {
        }

        @Autowired
        private MockMvc mockMvc;

        private final AssistantService service = new AssistantService();

        @Test
        void getAssistant_existing_returns200() throws Exception {
                // Erst Assistent anlegen
                mockMvc.perform(post("/api/v1/assistant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                { "name": "joule" ,
                                                "response": "Hello, I am Joule!" }
                                                """))
                                .andExpect(status().isCreated());

                // Dann abrufen
                mockMvc.perform(get("/api/v1/assistant/joule"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("joule"));
        }

        @Test
        void getAssistant_localy() throws Exception {
                mockMvc.perform(post("/api/v1/assistant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                { "name": "joule" ,
                                                "response": "Hello, I am Joule!" }
                                                """))
                                .andExpect(status().isCreated());
                Assistant assistant = service.getAssistant("joule");
                assertNotNull(assistant);
        }

        @Test
        void getAssistant_notExisting_returns404() throws Exception {
                mockMvc.perform(get("/api/v1/assistant/jule"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getAssistant_withEmptyName_returns400() throws Exception {
                mockMvc.perform(get("/api/v1/assistant/ "))
                                .andExpect(status().isBadRequest());
        }

}
