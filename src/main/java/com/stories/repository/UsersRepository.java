package com.stories.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stories.model.UserModel;

@Repository
public interface UsersRepository extends MongoRepository<UserModel, String> {

}
