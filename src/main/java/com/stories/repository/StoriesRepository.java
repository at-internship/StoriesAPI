package com.stories.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stories.model.StoryModel;
import com.stories.model.TaskModel;

@Repository
public interface StoriesRepository extends MongoRepository<StoryModel, String> {
	public TaskModel findByTasks__id(String id);
}