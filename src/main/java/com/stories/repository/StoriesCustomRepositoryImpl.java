package com.stories.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.stories.domain.TasksDomain;
import com.stories.model.TaskModel;

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
	
	@Override
	public AggregationResults<TaskModel> getTaskById(String _id){
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.unwind("tasks"),
				Aggregation.match(Criteria.where("tasks._id").is(new ObjectId(_id))),
				Aggregation.project("tasks._id", "tasks.name", "tasks.description",
									"tasks.status", "tasks.comments", "tasks.assignee")
				);
		AggregationResults<TaskModel> results = mongoTemplate.aggregate(aggregation, "stories", TaskModel.class);
		return results;
	}
	
}