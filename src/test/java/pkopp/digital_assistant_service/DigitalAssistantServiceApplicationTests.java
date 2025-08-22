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
class DigitalAssistantServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	private final AssistantService service = new AssistantService();

	@Test
	void helloEndpoint_returns_200() throws Exception {
		mockMvc.perform(get("/api/v1/hello"))
				.andExpect(status().isOk());
	}

	@Test
	void helloEndpoint_returns_response() throws Exception {
		mockMvc.perform(get("/api/v1/hello"))
				.andExpect(content().string("Hello, World!"));
	}

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

}
