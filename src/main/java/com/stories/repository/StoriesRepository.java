package com.stories.repository;

import com.stories.model.StoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoriesRepository extends MongoRepository<StoryModel, String> {

}