package interview.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
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
public class UserAccountResourceTests {

    @Autowired
    private MockMvc mvc;
	
	@Autowired
	UserAccountRepository accountRepo;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@After
	public void afterEach() throws Exception {
		accountRepo.deleteAll();
	}

	@Test
	public void testUserAccountRetrieval() throws Exception {
		accountRepo.save(new UserAccount(1,"user1","password1","role1"));
		mvc.perform(MockMvcRequestBuilders.get("/user/all").accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(
						equalTo("[{\"id\":1,\"username\":\"user1\",\"password\":\"password1\",\"role\":\"role1\"}]")));
	}
	
	@Test
	public void testUserAccountPosting() throws Exception {
		UserAccount user1 = new UserAccount(1,"userA","password","ADMIN");
		UserAccount user2 = new UserAccount(5,"userB","password","USER");
		
		mvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		
		mvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user2))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		
		mvc.perform(MockMvcRequestBuilders.get("/user/all"))
			.andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("userA"))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("ADMIN"))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("userB"))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(5))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("USER"));    
	}
	
	@Test
	public void testUserAccountPostingPasswordHashing() throws Exception {
		UserAccount user1 = new UserAccount(1,"userA","password","ADMIN");
		mvc.perform(MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		
		
		mvc.perform(MockMvcRequestBuilders.get("/user/all"))
			.andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	        .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").isString());
	}
}
