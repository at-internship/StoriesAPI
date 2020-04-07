package com.stories.repository;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.stories.domain.TasksDomain;
import com.stories.model.TaskModel;

public interface StoriesCustomRepository {
	
	AggregationResults<TasksDomain> getTasksByStory(String id);

	AggregationResults<TaskModel> getTaskById(String id);
}
