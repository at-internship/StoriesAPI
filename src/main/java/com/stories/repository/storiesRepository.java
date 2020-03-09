package com.stories.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stories.model.stories;
	
@Repository
public interface storiesRepository extends MongoRepository<stories, String> {
		
	
}