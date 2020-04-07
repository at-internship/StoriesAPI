package com.stories.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import com.stories.domain.TasksDomain;

@Repository
public class StoriesCustomRepositoryImpl implements StoriesCustomRepository {

private MongoTemplate mongoTemplate;
	
	public StoriesCustomRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public AggregationResults<TasksDomain> findAllTasksByStoryId(Aggregation aggregation) {
		AggregationResults<TasksDomain> results = mongoTemplate.aggregate(aggregation,"stories", TasksDomain.class);

		return results;
		
	}
	
}