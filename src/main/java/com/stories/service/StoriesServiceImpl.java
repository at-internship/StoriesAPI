package com.stories.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	@Autowired
	SprintsClient sprintClient;

	@Override
	public String createStory(StoryDomain storyDomain) throws Exception {
		storyDomain.setStart_date(dateValidation(storyDomain.getStart_date()));
		String[] mensaggeDinamicValidation = dynamicValidation(storyDomain);

		if (StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
			storyModel = mapperFacade.map(storyDomain, StoryModel.class);
			logger.debug("Creating story with the json : {}", storyModel);
			String id = nameValidation(storyModel).get_id();
			return id;
		} else {
			if (mensaggeDinamicValidation[2] == "BAD_REQUEST") {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], "", mensaggeDinamicValidation[1]);
			} else if (mensaggeDinamicValidation[2] == "CONFLICT") {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], HttpStatus.CONFLICT,
						mensaggeDinamicValidation[1]);
			} else {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], mensaggeDinamicValidation[1]);
			}
		}
	}

	@Override
	public String createTask(TasksDomain taskDomain, String id) throws Exception {
		TaskModel taskModel = new TaskModel();
		if (storiesRepository.existsById(id)) {
			if (userNullTaskValidation(taskDomain.getAssignee())) {
				if (statusTaskValidation(statusArray, taskDomain.getStatus())) {
					storyModel = storiesRepository.findById(id).get();
					List<TaskModel> tasks = storyModel.getTasks();
					taskModel = mapperFacade.map(taskDomain, TaskModel.class);
					taskModel.set_id(new ObjectId().toString());
					tasks.add(taskModel);
					storyModel.setTasks(tasks);
					storiesRepository.save(storyModel);
					return taskModel.get_id();
				} else {
					throw new EntityNotFoundException(
							"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
							"400", "/stories/");
				}
			} else {
				throw new EntityNotFoundException("The user provided does not exist", "/users/");
			}
		} else {
			throw new EntityNotFoundException("Story not found", HttpStatus.CONFLICT, "/stories/");
		}
	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found", "/stories/");
		} else
			logger.debug("Deleting story with the id: " + id);
		storiesRepository.deleteById(id);
	}

	@Override
	public void deleteTask(String id, String taskId) throws Exception {
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
						throw new EntityNotFoundException("Task with the given id was not found.", HttpStatus.CONFLICT,
								"/tasks/");
					}
				}
			} else {
				throw new EntityNotFoundException("There are not tasks for this user story yet.", "/tasks/");
			}
		}
	}

	@Override
	public StoryDomain updateStory(StoryDomain storyDomain, String id) throws Exception {
		storyDomain.setStart_date(dateValidation(storyDomain.getStart_date()));
		String[] mensaggeDinamicValidation = dynamicValidation(storyDomain);

		if (StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
			if (storiesRepository.existsById(id)) {
				storyModel = mapperFacade.map(storyDomain, StoryModel.class);
				storyModel.set_id(id);
				nameValidation(storyModel);
				storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
				logger.debug("Updating story with the id: " + id + " - JSON : {}", storyDomain);
				return storyDomain;
			} else {
				throw new EntityNotFoundException("Story not found", "/stories/");
			}
		} else {
			if (mensaggeDinamicValidation[2] == "BAD_REQUEST") {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], "", mensaggeDinamicValidation[1]);
			} else if (mensaggeDinamicValidation[2] == "CONFLICT") {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], HttpStatus.CONFLICT,
						mensaggeDinamicValidation[1]);
			} else {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], mensaggeDinamicValidation[1]);
			}
		}
	}

	@Override
	public TasksDomain updateTaskById(TasksDomain task, String id, String _id) throws Exception {
		if (storiesRepository.existsById(id)) {
			task.set_id(_id);
			TaskModel taskModel = storiesCustomRepository.getTaskById(id, _id).getUniqueMappedResult();
			if (taskModel == null) {
				throw new EntityNotFoundException("Task not found", "/stories/" + id + "/tasks/" + _id);
			}
			if (!statusTaskValidation(statusArray, task.getStatus())) {
				throw new EntityNotFoundException(
						"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
						"400", "/stories/");
			}
			if (!StringUtils.isEmpty(task.getAssignee())) {
				if (!usersRepository.existsById(task.getAssignee())) {
					throw new EntityNotFoundException("User assignee id does not exist", HttpStatus.CONFLICT,
							"/stories/" + id + "/tasks/" + _id);
				}
			}
			if (StringUtils.isEmpty(task.getName())) {
				throw new EntityNotFoundException("name is a required field", HttpStatus.BAD_REQUEST,
						"/stories/" + id + "/tasks/" + _id);
			}
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
		} else {
			throw new EntityNotFoundException("Story not found", "/stories/" + id);
		}
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", "/stories/");
		storyModel = storiesRepository.findById(id).get();
		storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
		logger.debug("Getting story with the id: " + id + " - JSON : {}", storyDomain);
		return storyDomain;
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
		if (storiesRepository.existsById(id)) {
			TaskModel taskModel = storiesCustomRepository.getTaskById(id, _id).getUniqueMappedResult();
			if (taskModel.get_id() == null) {
				throw new EntityNotFoundException("Task not found", "/stories/" + id + "/tasks/" + _id);
			}
			TasksDomain taskDomain = mapperFacade.map(taskModel, TasksDomain.class);
			return taskDomain;
		}
		throw new EntityNotFoundException("Story not found", "/stories/" + id);
	}

	public List<TasksDomain> getTasksByStory(String id) throws EntityNotFoundException {
		if (storiesRepository.existsById(id)) {
			List<TasksDomain> results = storiesCustomRepository.getTasksByStory(id).getMappedResults();
			return results;
		}
		throw new EntityNotFoundException("Story not found", "/stories/" + id + "/tasks");

	}

	private String statusValidation(String[] statusArray, String storyStatus) {
		String validationStatus = "";
		if (!(Arrays.asList(statusArray).contains(storyStatus))) {
			validationStatus = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.";
		}
		if (StringUtils.isEmpty(validationStatus)) {
			return validationStatus;
		} else {
			return validationStatus + ", ";
		}
	}

	private StoryModel nameValidation(StoryModel storyModel) throws EntityNotFoundException {
		try {
			storiesRepository.save(storyModel);
			return storyModel;
		} catch (Exception e) {
			throw new EntityNotFoundException("There is a story with this name already.", "", "/stories/");
		}
	}

	private String userNullValidation(String assigneeId) {
		String validation = "";
		if (StringUtils.isEmpty(assigneeId)) {

		} else {
			if (!usersRepository.existsById(assigneeId))
				validation = "User assignee_id does not exist";
			return validation;
		}
		return validation;
	}

	private String sprintNullValidation(String sprintId) {
		String validation = "";
		if (StringUtils.isEmpty(sprintId)) {

		} else {
			if (!sprintClient.existsSprintById(sprintId))
				validation = "The id entered in the sprint_id field does not exist";
		}
		if (StringUtils.isEmpty(validation)) {
			return validation;
		} else {
			return validation + ", ";
		}
	}

	private String nameStatusNullValidation(String name, String status) {
		String validationNameStatus = "";

		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(status)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required fields ('Name','Status').";
		} else if (StringUtils.isEmpty(name)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required field ('Name').";
		} else if (StringUtils.isEmpty(status)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required field ('Status').";
		}

		if (!StringUtils.isEmpty(validationNameStatus)) {
			return validationNameStatus;
		} else {
			return validationNameStatus;
		}
	}

	private LocalDate dateValidation(LocalDate date) {
		if ((!(date == null || (StringUtils.isEmpty(date.toString()))))) {
			return date;
		} else {
			return LocalDate.now();
		}
	}

	private String proggressValidation(int progress) {
		String progressValidation = "";
		if (progress < 0) {
			progressValidation = "The number entered in the progress field is a negative number";
		}

		if (progress > 100) {
			progressValidation = "The number entered in the progress field exceeds 100%";
		}

		if (StringUtils.isEmpty(progressValidation)) {
			return progressValidation;
		} else {
			return progressValidation + ", ";
		}
	}

	private String pointsValidation(int points, int[] pointsArray) {
		String pointsValidation = "";
		if (points < 0) {
			pointsValidation = "The number entered in the points field is a negative number";
		} else {
			for (int i = 0; i < pointsArray.length; i++) {
				if (points == pointsArray[i]) {
					break;
				} else if (i == pointsArray.length - 1) {
					pointsValidation = "The number entered in the points field does not match a valid story point";
				}
			}
		}
		if (StringUtils.isEmpty(pointsValidation)) {
			return pointsValidation;
		} else {
			return pointsValidation + ", ";
		}
	}

	private String[] dynamicValidation(StoryDomain storyDomain) {
		String[] mensaggeDinamicValidation = { "", "", "" };
		String validationRespons = "";
		String[] validationPath = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };

		validationRespons = nameStatusNullValidation(storyDomain.getName(), storyDomain.getStatus());
		if (!StringUtils.isEmpty(validationRespons)) {
			mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
			mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";
			mensaggeDinamicValidation[2] = "BAD_REQUEST";
			return mensaggeDinamicValidation;
		}

		if ((!StringUtils.isEmpty(storyDomain.getSprint_id()))
				|| (!StringUtils.isEmpty(storyDomain.getAssignee_id()))) {
			validationRespons = sprintNullValidation(storyDomain.getSprint_id());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";

			}

			validationRespons = userNullValidation(storyDomain.getAssignee_id());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";
			}

			mensaggeDinamicValidation[1] = filtervalidation(validationPath, mensaggeDinamicValidation[1]);
			mensaggeDinamicValidation[2] = "CONFLICT";
			if (!StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
				return mensaggeDinamicValidation;
			}
		}

		if ((!StringUtils.isEmpty(storyDomain.getStatus())) || !(StringUtils.isEmpty(storyDomain.getProgress() + ""))
				|| !(StringUtils.isEmpty(storyDomain.getPoints() + ""))) {
			validationRespons = pointsValidation(storyDomain.getPoints(), pointsArray);
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";
			}

			validationRespons = proggressValidation(storyDomain.getProgress());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";
			}

			validationRespons = statusValidation(statusArray, storyDomain.getStatus());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + "/stories/";
			}

			mensaggeDinamicValidation[1] = filtervalidation(validationPath, mensaggeDinamicValidation[1]);
			mensaggeDinamicValidation[2] = "BAD_REQUEST";
			if (!StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
				return mensaggeDinamicValidation;
			}
		}

		mensaggeDinamicValidation[1] = validationRespons;
		return mensaggeDinamicValidation;
	}

	private String filtervalidation(String[] validationPath, String string) {
		String validationRespons = "";
		for (int i = 0; i < validationPath.length; i++) {
			if (string.toString().indexOf(validationPath[i]) == -1) {

			} else {
				if (validationPath[i].equals(string.toString().substring(string.toString().indexOf(validationPath[i]),
						string.toString().indexOf(validationPath[i]) + validationPath[i].length()))) {
					if (!(string.toString().indexOf(validationPath[i]) == -1)) {
						if (StringUtils.isEmpty(validationRespons)) {
							validationRespons = validationPath[i];
						}
					}
				}
			}
		}
		return validationRespons;
	}

	private boolean userNullTaskValidation(String assigneeId) throws EntityNotFoundException {
		if (StringUtils.isEmpty(assigneeId)) {
			return true;
		} else {
			if (!usersRepository.existsById(assigneeId))
				throw new EntityNotFoundException("The user provided does not exist", HttpStatus.CONFLICT, "/stories/");
			return true;
		}
	}

	private boolean statusTaskValidation(String[] statusArray, String status) {
		if (StringUtils.isEmpty(status)) {
			return true;
		} else {
			return Arrays.asList(statusArray).contains(status);
		}
	}
}