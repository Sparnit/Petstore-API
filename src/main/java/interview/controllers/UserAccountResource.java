package interview.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import interview.dataaccessors.UserAccountRepository;
import interview.models.UserAccount;


@RestController
@CrossOrigin
public class UserAccountResource {
	// used to grab logger from logback default spring boot logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserAccountRepository userRepository;

	/**
	 * finds all users and return them
	 * 
	 * @return List<UserAccount>
	 */
	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	public List<UserAccount> getAll() {
		logger.info("Grabbing all Pets");
		List<UserAccount> accounts = userRepository.findAll();
		return accounts;
	}

	/**
	 * Used to post a user
	 * 
	 * @param user
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public void addPet(@RequestBody UserAccount user, HttpServletResponse response) {
		logger.info("saving user" + user.getUsername());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordEncoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncoded);
		userRepository.save(user);
		logger.info("saved user" + user.getUsername());
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
}