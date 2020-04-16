package com.stories.service;

import java.util.List;

import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;

public interface StoriesService {

	StoryDomain getStoryById(String id) throws Exception;

	List<StoryDomain> getAllStories() throws Exception;

	public String createStory(StoryDomain request) throws Exception;

	void deleteStory(String id) throws Exception;

	StoryDomain updateStory(StoryDomain request, String id) throws Exception;

	List<TasksDomain> getTasksByStory(String id) throws EntityNotFoundException;

	TasksDomain getTaskById(String id, String _id) throws Exception;

	void deleteTask(String id, String taskId) throws Exception;

	public String createTask(TasksDomain taskDomain, String id) throws Exception;

	public TasksDomain updateTaskById(TasksDomain task, String id, String _id) throws Exception;
}