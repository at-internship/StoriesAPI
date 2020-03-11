package com.stories.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stories.model.StoryModel;
	
public interface StoriesRepository extends MongoRepository<StoryModel, String> {
	
	List<StoryModel> findBytechnology(String technology);

	void save(com.stories.domain.StoryDomain storyDomain);

//	void save(com.stories.model.Story user);
	
}
