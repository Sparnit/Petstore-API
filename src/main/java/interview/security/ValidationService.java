package interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import interview.dataaccessors.UserAccountRepository;
import interview.models.UserAccount;

@Component
public class ValidationService {
	@Autowired
	UserAccountRepository userRepository;
	// used by services to verify is user is allowed to issue requests against service
	public boolean validateUserRole(String pass, String requiredRole){
		UserAccount account = userRepository.findByPassword(pass);
		if(account != null){
			if(account.getRole().equals(requiredRole)){
				return true;
			}
		}
		return false;
	}
	
	// used by services to verify had a correct token and is allowed to issue requests against service
		public boolean validateUser(String pass){
			UserAccount account = userRepository.findByPassword(pass);
			if(account != null){
				return true;
			}
			return false;
		}
}
