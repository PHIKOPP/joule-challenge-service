package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DigitalAssistantServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

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

}
