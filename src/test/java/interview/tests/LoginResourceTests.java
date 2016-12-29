package interview.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import interview.dataaccessors.UserAccountRepository;
import interview.models.UserAccount;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginResourceTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserAccountRepository accountRepo;

	@Autowired
	ObjectMapper objectMapper;

	UserAccount user;

	@Before
	public void beforeEach() throws Exception {
		UserAccount user1 = new UserAccount(1, "userA", "password", "ADMIN");
		mvc.perform(MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		user = accountRepo.findByUsername("userA");
	}

	@After
	public void afterEach() throws Exception {
		accountRepo.deleteAll();
	}

	@Test
	public void testLoginCorrectCredentials() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n\t\"username\":\"userA\",\n\t\"password\":\"password\"\n}"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
				.andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"));
	}

	@Test
	public void testLoginInCorrectCredentials() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n\t\"username\":\"userA\",\n\t\"password\":\"badPass\"\n}"))
				.andExpect(status().isForbidden());
	}
}
