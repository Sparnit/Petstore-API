package interview.dataaccessors;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import interview.models.Pet;
import interview.models.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, Integer>{
	public UserAccount findByUsername(String username);
	public UserAccount findByPassword(String password);
}