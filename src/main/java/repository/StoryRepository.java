package repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import modelo.Story;
	
public interface StoryRepository extends MongoRepository<Story, String> {
		
	}
