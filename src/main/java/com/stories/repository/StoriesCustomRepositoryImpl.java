package com.stories.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.stories.domain.TasksDomain;

@Repository
public class StoriesCustomRepositoryImpl implements StoriesCustomRepository {

private MongoTemplate mongoTemplate;
	
	public StoriesCustomRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public AggregationResults<TasksDomain> getTasksByStory(String id) {	
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.unwind("tasks"),
				Aggregation.match(Criteria.where("_id").is(id)),
				Aggregation.project("tasks._id", "tasks.name", "tasks.description", "tasks.status", "tasks.comments", "tasks.assignee")
			);
		AggregationResults<TasksDomain> results = mongoTemplate.aggregate(aggregation,"stories", TasksDomain.class);

		return results;
		
	}
	
}