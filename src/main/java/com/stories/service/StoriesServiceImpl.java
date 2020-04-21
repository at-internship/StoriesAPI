package com.stories.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.model.TaskModel;
import com.stories.repository.StoriesCustomRepository;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;
import com.stories.sprintsclient.SprintsClient;
import com.stories.validations.DynamicValidation;
import com.stories.validations.DynamicValidationArray;

import io.micrometer.core.instrument.util.StringUtils;
import ma.glasnost.orika.MapperFacade;

@Service
public class StoriesServiceImpl implements StoriesService {

	@Autowired
	StoriesRepository storiesRepository;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	StoriesCustomRepository storiesCustomRepository;

	private static Logger logger = LogManager.getLogger();

	@Autowired
	private MapperFacade mapperFacade;

	String[] statusArray = { "Refining", "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
	String[] DomainValidation = { "Sprint_id", "Technology", "Description", "Acceptance_criteria", "Points", "Progress",
			"Notes", "Comments", "Start_date", "Due_date", "Priority", "Assignee_id", "History" };
	int[] pointsArray = { 0, 1, 2, 3, 5 };
	StoryModel storyModel = new StoryModel();
	List<StoryModel> storiesModel = new ArrayList<StoryModel>();
	StoryDomain storyDomain = new StoryDomain();
	List<StoryDomain> storiesDomain = new ArrayList<>();
	DynamicValidationArray messageDinamicValidation = new DynamicValidationArray();

	@Autowired
	SprintsClient sprintClient;

	@Autowired
	DynamicValidation dynamicValidation;

	@Override
	public String createStory(StoryDomain storyDomain) throws Exception {
		storyDomain.setStart_date(dynamicValidation.startDate(storyDomain.getStart_date()));
		storyDomain.setDue_date(dynamicValidation.dueDate(storyDomain.getDue_date()));
		messageDinamicValidation = dynamicValidation.storyValidation(storyDomain, "");

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			storyModel = mapperFacade.map(storyDomain, StoryModel.class);
			logger.debug("Creating story with the json : {}", storyModel);
			String id = dynamicValidation.nameValidation(storyModel).get_id();
			return id;
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), HttpStatus.CONFLICT,
						messageDinamicValidation.getPath());
			} 
		}
	}

	@Override
	public String createTask(TasksDomain taskDomain, String id) throws Exception {
		TaskModel taskModel = new TaskModel();
		messageDinamicValidation = dynamicValidation.taskValidation(taskDomain, id, "");

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (!storiesRepository.existsById(id)) {
				throw new EntityNotFoundException("Story not found", HttpStatus.CONFLICT, "/stories/");
			}
			storyModel = storiesRepository.findById(id).get();
			List<TaskModel> tasks = storyModel.getTasks();
			taskModel = mapperFacade.map(taskDomain, TaskModel.class);
			taskModel.set_id(new ObjectId().toString());
			tasks.add(taskModel);
			storyModel.setTasks(tasks);
			storiesRepository.save(storyModel);
			return taskModel.get_id();
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), HttpStatus.CONFLICT,
						messageDinamicValidation.getPath());
			} 
		}
	}

	@Override
	public void deleteStory(String id) throws Exception {
		messageDinamicValidation = dynamicValidation.idValidation(id, "");

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (!storiesRepository.existsById(id)) {
				throw new EntityNotFoundException("Story with the given id was not found", "/stories/");
			} else
				logger.debug("Deleting story with the id: " + id);
			storiesRepository.deleteById(id);
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			}
		}
	}

	@Override
	public void deleteTask(String id, String taskId) throws Exception {
		messageDinamicValidation = dynamicValidation.idValidation(id, taskId);

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (!storiesRepository.existsById(id)) {
				throw new EntityNotFoundException("Story with the given id was not found", "/stories/");
			} else {
				storyModel = storiesRepository.findById(id).get();
				List<TaskModel> tasks = storyModel.getTasks();
				if (!tasks.isEmpty()) {
					for (int i = 0; i < tasks.size(); i++) {
						if (tasks.get(i).get_id().toString().equals(taskId)) {
							tasks.remove(i);
							logger.debug("Deleting task with the id: " + taskId);
							storyModel.setTasks(tasks);
							storiesRepository.save(storyModel);
						} else if (i == (tasks.size() - 1)) {
							throw new EntityNotFoundException("Task with the given id was not found.",
									HttpStatus.CONFLICT, "/tasks/");
						}
					}
				} else {
					throw new EntityNotFoundException("There are not tasks for this user story yet.", "/tasks/");
				}
			}
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			}
		}
	}

	@Override
	public StoryDomain updateStory(StoryDomain storyDomain, String id) throws Exception {
		storyDomain.setStart_date(dynamicValidation.startDate(storyDomain.getStart_date()));
		storyDomain.setDue_date(dynamicValidation.dueDate(storyDomain.getDue_date()));
		messageDinamicValidation = dynamicValidation.storyValidation(storyDomain, id);
		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (storiesRepository.existsById(id)) {
				List<TaskModel> tasksModel = storiesRepository.findById(id).get().getTasks();
				System.err.println(storyModel);
				storyModel = mapperFacade.map(storyDomain, StoryModel.class);
				storyModel.set_id(id);
				storyModel.setTasks(tasksModel);
				dynamicValidation.nameValidation(storyModel);
				storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
				logger.debug("Updating story with the id: " + id + " - JSON : {}", storyDomain);
				return storyDomain;
			} else {
				throw new EntityNotFoundException("Story not found", "/stories/");
			}
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), HttpStatus.CONFLICT,
						messageDinamicValidation.getPath());
			} 
		}
	}

	@Override
	public TasksDomain updateTaskById(TasksDomain task, String id, String _id) throws Exception {
		messageDinamicValidation = dynamicValidation.taskValidation(task, id, _id);
		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (!storiesRepository.existsById(id)) {
				throw new EntityNotFoundException("Story not found", "/stories/" + id);
			}
			task.set_id(_id);
			TaskModel taskModel = storiesCustomRepository.getTaskById(id, _id).getUniqueMappedResult();
			if (taskModel == null) {
				throw new EntityNotFoundException("Task not found", "/stories/" + id + "/tasks/" + _id);
			}

			if (storiesRepository.existsById(id)) {
				storyModel = storiesRepository.findById(id).get();
				List<TaskModel> updatedTasks = new ArrayList<>();
				for (TaskModel tasks : storyModel.getTasks()) {
					if (task.get_id().equals(tasks.get_id())) {
						tasks.set_id(task.get_id());
						tasks.setName(task.getName());
						tasks.setDescription(task.getDescription());
						tasks.setStatus(task.getStatus());
						tasks.setComments(task.getComments());
						tasks.setAssignee(task.getAssignee());
					}
					updatedTasks.add(tasks);
				}
				storyModel.setTasks(updatedTasks);
				storiesRepository.save(storyModel);
				return task;
			}
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), HttpStatus.CONFLICT,
						messageDinamicValidation.getPath());
			}
		}
		return task;
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		messageDinamicValidation = dynamicValidation.idValidation(id, "");

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (!storiesRepository.existsById(id))
				throw new EntityNotFoundException("Story not found", "/stories/");
			storyModel = storiesRepository.findById(id).get();
			storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
			logger.debug("Getting story with the id: " + id + " - JSON : {}", storyDomain);
			return storyDomain;
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(),
						messageDinamicValidation.getPath());
			}
		}
	}

	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> allStoriesModel = new ArrayList<>();
		List<StoryDomain> allStoriesDomain = new ArrayList<>();
		allStoriesModel = storiesRepository.findAll();
		if (allStoriesModel == null)
			throw new EntityNotFoundException("Stories not found", "/stories/");
		for (int i = 0; i < allStoriesModel.size(); i++) {
			allStoriesDomain.add(mapperFacade.map(allStoriesModel.get(i), StoryDomain.class));
		}
		logger.debug("Getting all stories - JSON : {}", allStoriesDomain);
		return allStoriesDomain;
	}

	@Override
	public TasksDomain getTaskById(String id, String _id) throws Exception {
		messageDinamicValidation = dynamicValidation.idValidation(id, _id);

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (storiesRepository.existsById(id)) {
				TaskModel taskModel = storiesCustomRepository.getTaskById(id, _id).getUniqueMappedResult();
				if (taskModel.get_id() == null) {
					throw new EntityNotFoundException("Task not found", "/stories/" + id + "/tasks/" + _id);
				}
				TasksDomain taskDomain = mapperFacade.map(taskModel, TasksDomain.class);
				return taskDomain;
			} else {
				throw new EntityNotFoundException("Story not found", "/stories/" + id);
			}
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(),
						messageDinamicValidation.getPath());
			}
		}
	}

	@Override
	public List<TasksDomain> getTasksByStory(String id) throws EntityNotFoundException {
		messageDinamicValidation = dynamicValidation.idValidation(id, "");

		if (ObjectUtils.isEmpty(messageDinamicValidation.getMessage())) {
			if (storiesRepository.existsById(id)) {
				List<TasksDomain> results = storiesCustomRepository.getTasksByStory(id).getMappedResults();
				return results;
			} else {
				throw new EntityNotFoundException("Story not found", "/stories/" + id + "/tasks");
			}
		} else {
			if (messageDinamicValidation.getStatus() == "BAD_REQUEST") {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(), "",
						messageDinamicValidation.getPath());
			} else {
				throw new EntityNotFoundException(messageDinamicValidation.getMessage().toString(),
						messageDinamicValidation.getPath());
			}
		}
	}
}