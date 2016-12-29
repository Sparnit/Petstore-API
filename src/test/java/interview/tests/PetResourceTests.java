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
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import interview.dataaccessors.PetRepository;
import interview.dataaccessors.UserAccountRepository;
import interview.models.Category;
import interview.models.Pet;
import interview.models.Tag;
import interview.models.UserAccount;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PetResourceTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	UserAccountRepository accountRepo;

	@Autowired
	PetRepository petRepo;

	@Autowired
	ObjectMapper objectMapper;

	UserAccount userAdmin;
	UserAccount userNotAdmin;

	Pet basePet;

	@Before
	public void beforeEach() throws Exception {
		// save 1 Pet for all test cases
		String[] photos = { "url1", "url2" };
		Tag[] tags = { new Tag(1, "tagName") };
		basePet = new Pet(1, new Category(1, "name"), "dog", photos, tags, "available");
		petRepo.save(basePet);

		UserAccount user1 = new UserAccount(1, "userA", "password", "ADMIN");
		mvc.perform(MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		userAdmin = accountRepo.findByUsername("userA");

		UserAccount user2 = new UserAccount(2, "userB", "password", "USER");
		mvc.perform(MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user2)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		userNotAdmin = accountRepo.findByUsername("userB");
	}

	// Get All Test Cases

	@Test
	public void testPetRetrievalGetAllWithValidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/pet/all").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", userAdmin.getPassword())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("dog"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("available"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[0].name").value("tagName"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].photoUrls[0]").value("url1"));

	}

	@Test
	public void testPetRetrievalGetAllWithInValidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/pet/all")
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "FAKE_TOKEN"))
				.andExpect(status().isForbidden());
	}

	// Deletion Test Cases
	@Test
	public void testPetDeletionInsufficientPrivledges() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/pet/1")
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", userNotAdmin.getPassword()))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testPetDeletionInvalidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/pet/1")
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "FAKE_TOKEN"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testPetDeletionValidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/pet/1")
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", userAdmin.getPassword()))
				.andExpect(status().isOk());

		// verify that no pet is left over
		Pet pet = petRepo.findById(1);
		assertNull(pet);
	}

	// Creation Test Cases
	@Test
	public void testPetCreationInsufficientPrivledges() throws Exception {
		String[] photos = { "url1", "url2" };
		Tag[] tags = { new Tag(1, "tagName") };
		Pet pet = new Pet(2, new Category(1, "name"), "dog", photos, tags, "available");

		mvc.perform(MockMvcRequestBuilders.post("/pet").content(objectMapper.writeValueAsString(pet))
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", userNotAdmin.getPassword()))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testPetCreationInvalidUser() throws Exception {
		String[] photos = { "url1", "url2" };
		Tag[] tags = { new Tag(1, "tagName") };
		Pet pet = new Pet(2, new Category(1, "name"), "dog", photos, tags, "available");

		mvc.perform(MockMvcRequestBuilders.post("/pet").content(objectMapper.writeValueAsString(pet))
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "FAKE_TOKEN"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testPetCreationWithValidUser() throws Exception {
		String[] photos = { "url1", "url2" };
		Tag[] tags = { new Tag(1, "tagName") };
		Pet pet = new Pet(2, new Category(1, "name"), "dog", photos, tags, "available");

		mvc.perform(MockMvcRequestBuilders.post("/pet").content(objectMapper.writeValueAsString(pet))
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", userAdmin.getPassword()))
				.andExpect(status().isCreated());
		Pet petInRepo = petRepo.findById(2);
		// Pet should now exist in repo
		assertNotNull(petInRepo);
		assertEquals(petInRepo.getName(),pet.getName());
		assertEquals(petInRepo.getId(),pet.getId());
		assertEquals(petInRepo.getPhotoUrls()[0],pet.getPhotoUrls()[0]);
		assertEquals(petInRepo.getStatus(),pet.getStatus());
	}
	
	@Test
	public void testPetRetrievalGetByIdWithValidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/pet/1").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", userAdmin.getPassword())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("dog"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("available"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.tags[0].name").value("tagName"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.photoUrls[0]").value("url1"));

	}

	@Test
	public void testPetRetrievalGetByIdWithInValidUser() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/pet/1")
				// should be returned
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "FAKE_TOKEN"))
				.andExpect(status().isForbidden());
	}

	@After
	public void afterEach() throws Exception {
		accountRepo.deleteAll();
		petRepo.deleteAll();
	}

}
