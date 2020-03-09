package com.stories.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stories.model.Story_task;

//import net.bytebuddy.dynamic.ClassFileLocator.AgentBased.ClassLoadingDelegate.ForDelegatingClassLoader.Dispatcher.Initializable;

public interface Story_task_repository extends MongoRepository<Story_task , Serializable> {

		
}
