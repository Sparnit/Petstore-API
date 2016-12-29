package interview.dataaccessors;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import interview.models.Pet;

public interface PetRepository extends MongoRepository<Pet, Integer>{
	public Pet findById(int id);
}