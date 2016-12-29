package interview.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoTimeoutException;

import interview.dataaccessors.PetRepository;
import interview.exceptions.ForbiddenException;
import interview.exceptions.InternalException;
import interview.models.Pet;
import interview.security.ValidationService;

@RestController
@CrossOrigin
public class PetResource {
	// used to grab logger from logback default spring boot logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PetRepository petRepository;

	@Autowired
	ValidationService validateService;

	/**
	 * Used to retrieve a specific pet
	 * 
	 * @param petId
	 * @return pet
	 */

	@RequestMapping(value = "/pet/{petId}", method = RequestMethod.GET)
	public Pet getPetById(@PathVariable int petId, @RequestHeader(value = "Authorization") String authHeader) {
	
		try {
			boolean allowed = validateService.validateUser(authHeader);
			if (allowed) {
				logger.info("Looking for Pet" + petId);
				return petRepository.findById(petId);
			} else {
				throw new ForbiddenException();
			}
		} catch (MongoTimeoutException e) {
			e.printStackTrace();
			throw new InternalException();
		}
	}

	/**
	 * finds all pets and return them
	 * 
	 * @return List<Pet>
	 */
	@RequestMapping(value = "/pet/all", method = RequestMethod.GET)
	public List<Pet> getAll(@RequestHeader(value = "Authorization") String authHeader) {
		try {
			boolean allowed = validateService.validateUser(authHeader);
			if (allowed) {
				logger.info("Grabbing all Pets");
				return petRepository.findAll();
			} else {
				throw new ForbiddenException();
			}
		} catch (MongoTimeoutException e) {
			e.printStackTrace();
			throw new InternalException();
		}
	}

	/**
	 * Used to delete a specific pet
	 * 
	 * @param petId
	 */
	@RequestMapping(value = "/pet/{petId}", method = RequestMethod.DELETE)
	public void deletePet(@PathVariable int petId, @RequestHeader(value = "Authorization") String authHeader) {
		try {
			logger.info("Deleting Pet with id" + petId);
			boolean allowed = validateService.validateUserRole(authHeader, "ADMIN");
			if (allowed) {
				petRepository.delete(petId);
			} else {
				throw new ForbiddenException();
			}
		} catch (MongoTimeoutException e) {
			e.printStackTrace();
			throw new InternalException();
		}
	}

	/**
	 * Used to post a pet
	 * 
	 * @param pet
	 */
	@RequestMapping(value = "/pet", method = RequestMethod.POST)
	public void addPet(@RequestBody Pet pet, HttpServletResponse response,
			@RequestHeader(value = "Authorization") String authHeader) {
		try {
			boolean allowed = validateService.validateUserRole(authHeader, "ADMIN");
			if (allowed) {
				logger.info("User Allowed " + allowed);
				logger.info("saving pet" + pet.getName());
				petRepository.save(pet);
				logger.info("saved pet" + pet.getName());
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				throw new ForbiddenException();
			}
		} catch (MongoTimeoutException e) {
			e.printStackTrace();
			throw new InternalException();
		}
	}
}