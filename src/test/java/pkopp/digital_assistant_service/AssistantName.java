package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class AssistantName {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    private final AssistantService service = new AssistantService();

    @Test
    void nameAssitant_returns201_andBody() throws Exception {
        mockMvc.perform(get("/api/v1/assistant/name?name=TestName"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Assistant name set to: TestName"));
    }

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

}
