package com.stories.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stories.model.Story;
	
public interface StoriesRepository extends MongoRepository<Story, String> {
		
	}
