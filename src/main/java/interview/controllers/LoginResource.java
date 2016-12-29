package interview.controllers;

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
import interview.exceptions.ForbiddenException;
import interview.models.Token;
import interview.models.UserAccount;

@RestController
@CrossOrigin
public class LoginResource {

	@Autowired
	UserAccountRepository userAccountRepository;

	/**
	 * Used to Login
	 * 
	 * @param user
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Token login(@RequestBody UserAccount user, HttpServletResponse response) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserAccount account = userAccountRepository.findByUsername(user.getUsername());
		if (account != null) {
			boolean allowed = passwordEncoder.matches(user.getPassword(), account.getPassword());
			if(allowed){
				return new Token(account.getPassword(), account.getRole());
			}
			
		}
		throw new ForbiddenException();
	}
}
