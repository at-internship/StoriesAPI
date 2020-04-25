package com.stories.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stories.constants.StoriesApiConstants;
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
	String[] storyDomainValidation = { "Story_id", "Sprint_id", "Status", "Priority", "Assignee_id" };
	String[] taskDomainValidation = { "Story_id", "Task_id", "Status", "assignee" };
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
		String[] mensaggeDinamicValidation = dynamicValidation(storyDomain, "");

		if (StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
			storyModel = mapperFacade.map(storyDomain, StoryModel.class);
			logger.debug("Creating story with the json : {}", storyModel);
			String id = nameValidation(storyModel).get_id();
			return id;
		} else {
			if (mensaggeDinamicValidation[2] == StoriesApiConstants.httpStatusBadRequest) {
				throw new EntityNotFoundException(mensaggeDinamicValidation[0], "", mensaggeDinamicValidation[1]);
			} else if (mensaggeDinamicValidation[2] == StoriesApiConstants.httpStatusConflict) {
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
		String mensaggeDinamicValidation = TaskSpecialCharacterValidation(taskDomain, id, "");
		if (!StringUtils.isEmpty(mensaggeDinamicValidation)) {
			throw new EntityNotFoundException(mensaggeDinamicValidation, "", StoriesApiConstants.pathStories);
		}
		   if(storiesRepository.existsById(id)) {
			   if(!StringUtils.isEmpty(taskDomain.getName())) {  
			      if (userNullTaskValidation(taskDomain.getAssignee())) {
			         if (statusTaskValidation(StoriesApiConstants.statusArray, taskDomain.getStatus())) {
			            storyModel = storiesRepository.findById(id).get();
			            List<TaskModel> tasks = storyModel.getTasks();
			            taskModel = mapperFacade.map(taskDomain, TaskModel.class);
			            taskModel.set_id(new ObjectId().toString());
			            tasks.add(taskModel);
			            storyModel.setTasks(tasks);
			            storiesRepository.save(storyModel);
			            return taskModel.get_id();
			         }else {
			            throw new EntityNotFoundException(
			            		StoriesApiConstants.storyFieldStatusInvalidException,
			            		StoriesApiConstants.httpCodeStatusBadRequest,
			            		StoriesApiConstants.pathStories);
			         }
			      }else {
			         throw new EntityNotFoundException(StoriesApiConstants.taskFieldAssigneeNotFoundException, StoriesApiConstants.pathStories);
			      }
			   }else {
				   throw new EntityNotFoundException(StoriesApiConstants.taskFieldNameRequiredException,
						   StoriesApiConstants.httpCodeStatusBadRequest,
						   StoriesApiConstants.pathStories);
			   }
		   }else {
		         throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories);
		      }
		}
	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException,StoriesApiConstants.pathStories);
		} else
			logger.debug("Deleting story with the id: " + id);
		storiesRepository.deleteById(id);
	}

	@Override
	public void deleteTask(String id, String taskId) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories);
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
						throw new EntityNotFoundException(StoriesApiConstants.taskFieldIdNotFoundException, 
								HttpStatus.CONFLICT,
								StoriesApiConstants.pathTasks);
					}
				}
			} else {
				throw new EntityNotFoundException(StoriesApiConstants.storyFieldTasksEmptyException, StoriesApiConstants.pathTasks);
			}
		}
	}

	@Override
	public StoryDomain updateStory(StoryDomain storyDomain, String id) throws Exception {
		storyDomain.setStart_date(dateValidation(storyDomain.getStart_date()));
		String[] mensaggeDinamicValidation = dynamicValidation(storyDomain, id);

		if (StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
			if (storiesRepository.existsById(id)) {
				List<TaskModel> tasksModel = storiesRepository.findById(id).get().getTasks();
				storyModel = mapperFacade.map(storyDomain, StoryModel.class);
				storyModel.set_id(id);
				storyModel.setTasks(tasksModel);
				nameValidation(storyModel);
				storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
				logger.debug("Updating story with the id: " + id + " - JSON : {}", storyDomain);
				return storyDomain;
			} else {
				throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories);
			}
		} else {
			if(mensaggeDinamicValidation[2] == StoriesApiConstants.httpStatusBadRequest) {
                throw new EntityNotFoundException(mensaggeDinamicValidation[0],"", mensaggeDinamicValidation[1]);
            }
            else if(mensaggeDinamicValidation[2] == StoriesApiConstants.httpStatusConflict) {
                throw new EntityNotFoundException(mensaggeDinamicValidation[0], HttpStatus.CONFLICT, mensaggeDinamicValidation[1]);
            }
            else {
                throw new EntityNotFoundException(mensaggeDinamicValidation[0], mensaggeDinamicValidation[1]);
            }
		}
	}

	@Override
	public TasksDomain updateTaskById(TasksDomain task, String id, String _id) throws Exception {
		String mensaggeDinamicValidation = TaskSpecialCharacterValidation(task, id, _id);
    if(!StringUtils.isEmpty(mensaggeDinamicValidation)) {
			throw new EntityNotFoundException(mensaggeDinamicValidation, "", StoriesApiConstants.pathStories);
		}
		if (storiesRepository.existsById(id)) {
			task.set_id(_id);
			if (!statusTaskValidation(StoriesApiConstants.statusArray, task.getStatus())) {
				throw new EntityNotFoundException(
						StoriesApiConstants.storyFieldStatusInvalidException,
						StoriesApiConstants.httpCodeStatusBadRequest, 
						StoriesApiConstants.pathStories);
			}
			if (!StringUtils.isEmpty(task.getAssignee())) {
				if (!usersRepository.existsById(task.getAssignee())) {
					throw new EntityNotFoundException(StoriesApiConstants.taskFieldAssigneeNotFoundException, HttpStatus.CONFLICT,
							StoriesApiConstants.pathStories + id + StoriesApiConstants.pathTasks + _id);
				}
			}
			if (StringUtils.isEmpty(task.getName())) {
				throw new EntityNotFoundException(StoriesApiConstants.taskFieldNameRequiredException, 
						StoriesApiConstants.httpCodeStatusBadRequest,
						StoriesApiConstants.pathStories + id + StoriesApiConstants.pathTasks + _id);
			}
			storyModel = storiesRepository.findById(id).get();
			List<TaskModel> updatedTasks = new ArrayList<>();
			boolean taskFoundFlag = false;
			for (TaskModel tasks : storyModel.getTasks()) {
				if (task.get_id().equals(tasks.get_id())) {
					tasks.set_id(task.get_id());
					tasks.setName(task.getName());
					tasks.setDescription(task.getDescription());
					tasks.setStatus(task.getStatus());
					tasks.setComments(task.getComments());
					tasks.setAssignee(task.getAssignee());
					taskFoundFlag = true;
				}
				updatedTasks.add(tasks);
			}
			if (!taskFoundFlag) {
				throw new EntityNotFoundException(StoriesApiConstants.taskFieldIdNotFoundException, StoriesApiConstants.pathStories + id + StoriesApiConstants.pathTasks + _id);
			}
			storyModel.setTasks(updatedTasks);
			storiesRepository.save(storyModel);
			return task;
		} else {
			throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories + id);
		}
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories);
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
			throw new EntityNotFoundException(StoriesApiConstants.storiesNotFoundException, StoriesApiConstants.pathStories);
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
					throw new EntityNotFoundException(StoriesApiConstants.taskFieldIdNotFoundException, StoriesApiConstants.pathStories + id + StoriesApiConstants.pathTasks + _id);
				}
				TaskModel storyWithId = storiesRepository.findByTasks__id(_id);
				if(storyWithId.get_id().equals(id)) {
					TasksDomain taskDomain = mapperFacade.map(taskModel, TasksDomain.class);
					return taskDomain;
				}
			throw new EntityNotFoundException(StoriesApiConstants.taskFieldIdNotFoundException, StoriesApiConstants.pathStories + id);
		}
		throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories + id);
	}

	public List<TasksDomain> getTasksByStory(String id) throws EntityNotFoundException {
		if (storiesRepository.existsById(id)) {
			List<TasksDomain> results = storiesCustomRepository.getTasksByStory(id).getMappedResults();
			return results;
		}
		throw new EntityNotFoundException(StoriesApiConstants.storyFieldIdNotFoundException, StoriesApiConstants.pathStories + id + StoriesApiConstants.pathTasks);
	}

	private String statusValidation(String[] statusArray, String storyStatus) {
		String validationStatus = "";
		if (!(Arrays.asList(statusArray).contains(storyStatus))) {
			validationStatus = StoriesApiConstants.storyFieldStatusInvalidException;
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
			throw new EntityNotFoundException(StoriesApiConstants.storyFieldNameExistException, "", StoriesApiConstants.pathStories);
		}
	}

	private String userNullValidation(String assigneeId) {
		String validation = "";
		if (StringUtils.isEmpty(assigneeId)) {

		} else {
			if (!usersRepository.existsById(assigneeId))
				validation = StoriesApiConstants.storyFieldAssigneDoesntExistException;
			return validation;
		}
		return validation;
	}

	private String sprintNullValidation(String sprintId) {
		String validation = "";
		if (StringUtils.isEmpty(sprintId)) {

		} else {
			if (!sprintClient.existsSprintById(sprintId))
				validation = StoriesApiConstants.storyFieldSprintIdDoesntExistException;
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
			validationNameStatus = StoriesApiConstants.storyFieldsNameAndStatusRequiredException;
		} else if (StringUtils.isEmpty(name)) {
			validationNameStatus = StoriesApiConstants.storyFieldNameRequiredException;
		} else if (StringUtils.isEmpty(status)) {
			validationNameStatus = StoriesApiConstants.storyFieldStatusRequiredException;
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
			progressValidation = StoriesApiConstants.storyFieldProgressNegativeException;
		}
		if (progress > 100) {
			progressValidation = StoriesApiConstants.storyFieldProgressExceedsException;
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
			pointsValidation = StoriesApiConstants.storyFieldPointsNegativeException;
		} else {
			for (int i = 0; i < pointsArray.length; i++) {
				if (points == pointsArray[i]) {
					break;
				} else if (i == pointsArray.length - 1) {
					pointsValidation = StoriesApiConstants.storyFieldPointsInvalidException;
				}
			}
		}
		if (StringUtils.isEmpty(pointsValidation)) {
			return pointsValidation;
		} else {
			return pointsValidation + ", ";
		}
	}

	private String[] dynamicValidation(StoryDomain storyDomain, String storyId) {
		String[] mensaggeDinamicValidation = { "", "", "" };
		String validationRespons = "";
		String[] validationPath = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };
		int countValidationPositive = 0;

		List<String> validateSpecialCharacterField = new ArrayList<>();
		validateSpecialCharacterField.add(storyId);
		validateSpecialCharacterField.add(storyDomain.getSprint_id());
		validateSpecialCharacterField.add(storyDomain.getStatus());
		validateSpecialCharacterField.add(storyDomain.getPriority());
		validateSpecialCharacterField.add(storyDomain.getAssignee_id());

		validationRespons = nameStatusNullValidation(storyDomain.getName(), storyDomain.getStatus());
		if (!StringUtils.isEmpty(validationRespons)) {
			mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
	    mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;
      mensaggeDinamicValidation[2] = StoriesApiConstants.httpStatusBadRequest;
			return mensaggeDinamicValidation;
		}

		validationRespons = "";
		for (int i = 0; i < validateSpecialCharacterField.size(); i++) {
			boolean err = Pattern.compile("[a-zA-Z0-9]*").matcher(validateSpecialCharacterField.get(i)).matches();
			if (!err) {
				countValidationPositive++;
				if (StringUtils.isEmpty(validationRespons)) {
					validationRespons = storyDomainValidation[i];
				} else {
					validationRespons = validationRespons + ", " + storyDomainValidation[i];
				}
			}
		}

	  if(countValidationPositive > 1) {
        	mensaggeDinamicValidation[0] = StoriesApiConstants.exceptionCharactersInFollowingFields + validationRespons;
  		}
  	else if(countValidationPositive == 1){
  			mensaggeDinamicValidation[0] = StoriesApiConstants.exceptionCharactersInSpecificField + validationRespons;
  	}

		if (!StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
      mensaggeDinamicValidation[1] = StoriesApiConstants.pathStories;
      mensaggeDinamicValidation[2] = StoriesApiConstants.httpStatusBadRequest;
			return mensaggeDinamicValidation;
		}

		if ((!StringUtils.isEmpty(storyDomain.getSprint_id())) || (!StringUtils.isEmpty(storyDomain.getAssignee_id()))) {
			validationRespons = sprintNullValidation(storyDomain.getSprint_id());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;

			}

			validationRespons = userNullValidation(storyDomain.getAssignee_id());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;
			}

			mensaggeDinamicValidation[1] = filtervalidation(validationPath, mensaggeDinamicValidation[1]);
			mensaggeDinamicValidation[2] = StoriesApiConstants.httpStatusConflict;
			if (!StringUtils.isEmpty(mensaggeDinamicValidation[0])) {
				return mensaggeDinamicValidation;
			}
		}

		if ((!StringUtils.isEmpty(storyDomain.getStatus())) || !(StringUtils.isEmpty(storyDomain.getProgress() + "")) || !(StringUtils.isEmpty(storyDomain.getPoints() + ""))) {
			validationRespons = pointsValidation(storyDomain.getPoints(), pointsArray);
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;
			}

			validationRespons = proggressValidation(storyDomain.getProgress());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;
			}

			validationRespons = statusValidation(statusArray, storyDomain.getStatus());
			if (!StringUtils.isEmpty(validationRespons)) {
				mensaggeDinamicValidation[0] = mensaggeDinamicValidation[0] + validationRespons;
				mensaggeDinamicValidation[1] = mensaggeDinamicValidation[1] + StoriesApiConstants.pathStories;
			}

			mensaggeDinamicValidation[1] = filtervalidation(validationPath, mensaggeDinamicValidation[1]);
			mensaggeDinamicValidation[2] = StoriesApiConstants.httpStatusBadRequest;
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
				throw new EntityNotFoundException(StoriesApiConstants.taskFieldAssigneeNotFoundException,HttpStatus.CONFLICT,StoriesApiConstants.pathStories);
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

	private String TaskSpecialCharacterValidation(TasksDomain task, String storyId, String taskId) {
		int countValidationPositive = 0;
		String validationRespons = "";
		List<String> validateSpecialCharacterField = new ArrayList<>();
		validateSpecialCharacterField.add(storyId);
		validateSpecialCharacterField.add(taskId);
		validateSpecialCharacterField.add(task.getStatus());
		validateSpecialCharacterField.add(task.getAssignee());

		validationRespons = "";
		for (int i = 0; i < validateSpecialCharacterField.size(); i++) {
			boolean err = Pattern.compile("[a-zA-Z0-9]*").matcher(validateSpecialCharacterField.get(i)).matches();
			if (!err) {
				countValidationPositive++;
				if (StringUtils.isEmpty(validationRespons)) {
					validationRespons = taskDomainValidation[i];
				} else {
					validationRespons = validationRespons + ", " + taskDomainValidation[i];
				}
			}
		}
	  if(countValidationPositive > 1) {
	  	validationRespons = StoriesApiConstants.exceptionCharactersInFollowingFields + validationRespons;
	  }
	  else if(countValidationPositive == 1){
	  	validationRespons = StoriesApiConstants.exceptionCharactersInSpecificField + validationRespons;
	  }
		return validationRespons;
	}
}