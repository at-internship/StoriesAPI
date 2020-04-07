package com.stories.repository;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.TaskModel;

public interface StoriesCustomRepository {
	
	AggregationResults<TasksDomain> getTasksByStory(String id);

	AggregationResults<TaskModel> getTaskById(String storyId, String _id) throws EntityNotFoundException;
}
