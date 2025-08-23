package pkopp.digital_assistant_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class HelloWorldTest {

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
}
