package com.stories.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;
	
public interface StoriesRepository extends MongoRepository<StoryModel, String> {

	void save(StoryDomain storyDomain);
	
}
