package com.stories.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.stories.domain.SprintDomain;
import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.model.StoryModel;
import com.stories.model.TaskModel;

@Component
public class TestUtils {

	private static UnitTestProperties unitTestProperties;
	@Autowired
	private TestUtils(UnitTestProperties unitTestProperties) {
		TestUtils.unitTestProperties = unitTestProperties;
	}

	public TestUtils() {
	}

	public RestTemplate getrestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	public StoryModel getEmtyStoryMdodel() {
		StoryModel storyModel = new StoryModel();
		return storyModel;
	}

	public StoryDomain getEmtyStoryDomain() {
		StoryDomain storyDomain = new StoryDomain();
		return storyDomain;
	}

	public List<StoryModel> getEmtyStoryModelList() {
		List<StoryModel> storiesModelList = new ArrayList<StoryModel>();
		return storiesModelList;
	}

	public List<SprintDomain> getEmtySprintsDomain() {
		List<SprintDomain> storyDomain = new ArrayList<SprintDomain>();
		return storyDomain;
	}
	
	public TasksDomain getEmptyTasksDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		return tasksDomain;
	}
	
	public TaskModel getEmptyTasksModel() {
		TaskModel taskModel = new TaskModel();
		return taskModel;
	}

	public String setStoryInJsonFormat(String id) {
		return "{\"id\":\"" + id
				+ "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String postStoryValidJson(String id) {
		return "{\"id\":\"" + id
				+ "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String postStoryInvalidStatusJson() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String setStoryInJsonBadFormat(String id) {
		return "{\"id\":\"" + id
				+ "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":\"2#\",\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String postStoryBadJsonFormat() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":\"213\",\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String getByid() {
		return "{\"sprint_id\":\"hola\", \"technology\":\"Java\",\"name\":\"Probando la impresion en consola y json file\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":1,\"progress\":1, \"status\":\"Working\",\"notes\":\"!\",\"comments\":\"$\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"%\", \"assignee_id\":\"Prueba\",\"history\":[\"1\",\"2\"]}";
	}

	public static StoryDomain getDummyStoryDomain() {
		LocalDate date = LocalDate.now();
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add("1");
		historyList.add("2");
		StoryDomain storyDomain = new StoryDomain();
		storyDomain.setSprint_id("Hello");
		storyDomain.setTechnology("Java");
		storyDomain.setName("Try Test");
		storyDomain.setDescription("");
		storyDomain.setAcceptance_criteria("");
		storyDomain.setPoints(1);
		storyDomain.setProgress(1);
		storyDomain.setStatus("Working");
		storyDomain.setNotes("!");
		storyDomain.setComments("$");
		storyDomain.setStart_date(date);
		storyDomain.setDue_date(date);
		storyDomain.setPriority("%");
		storyDomain.setAssignee_id("Try Test");
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
		
	public static StoryModel getDummyStoryModel() {
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.modelHistory1);
		historyList.add(unitTestProperties.modelHistory2);
		StoryModel storyModel = new StoryModel();
		storyModel.set_id(unitTestProperties.modelId);
		storyModel.setSprint_id(unitTestProperties.modelSprintid);
		storyModel.setTechnology(unitTestProperties.modelTechnology);
		storyModel.setName(unitTestProperties.modelName);
		storyModel.setDescription(unitTestProperties.modelDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.modelAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.modelNotes);
		storyModel.setComments(unitTestProperties.modelComments);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.modelDueDate);
		storyModel.setPriority(unitTestProperties.modelPriority);
		storyModel.setAssignee_id(unitTestProperties.modelAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static List<StoryModel> listStoriesModelNull() {
		List<StoryModel> storiesModel = new ArrayList<StoryModel>();
		storiesModel = null;
		return storiesModel;
	}

	public static StoryDomain getStoryDomain() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);

		return storyDomain;
	}
	
	public static TasksDomain getTasksDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id(unitTestProperties.TasksdomainId);
		tasksDomain.setName(unitTestProperties.TasksdomainName);
		tasksDomain.setDescription(unitTestProperties.TasksdomainDescription);
		tasksDomain.setStatus(unitTestProperties.TasksdomainStatus);
		tasksDomain.setComments(unitTestProperties.TasksdomainComments);
		tasksDomain.setAssignee(unitTestProperties.TasksdomainAssignee);
		return tasksDomain;
	}
 	
 	public static List<TasksDomain> getTasksDomainList(){
 		List<TasksDomain> tasksDomainList= new ArrayList<TasksDomain>();
 		TasksDomain tasksDomain = new TasksDomain();
 		tasksDomain.set_id(unitTestProperties.domainId);
 		tasksDomain.setName(unitTestProperties.domainName);
 		tasksDomain.setDescription(unitTestProperties.domainDescription);
 		tasksDomain.setStatus(unitTestProperties.domainStatus);
 		tasksDomain.setComments(unitTestProperties.domainComment);
 		tasksDomain.setAssignee(unitTestProperties.domainAssigneeId);
 		tasksDomainList.add(tasksDomain);
		return tasksDomainList;
 	}
 	
 	public static TaskModel getTasksModel() {
		TaskModel tasksModel = new TaskModel();
		tasksModel.set_id(unitTestProperties.TasksModelId);
		tasksModel.setName(unitTestProperties.TasksdomainName);
		tasksModel.setDescription(unitTestProperties.TasksdomainDescription);
		tasksModel.setStatus(unitTestProperties.TasksdomainStatus);
		tasksModel.setComments(unitTestProperties.TasksdomainComments);
		tasksModel.setAssignee(unitTestProperties.TasksdomainAssignee);
		return tasksModel;
	}

	public static StoryModel getStoryModel() {
		StoryModel storyModel = new StoryModel();
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.modelHistory1);
		historyList.add(unitTestProperties.modelHistory2);
		storyModel.set_id(unitTestProperties.modelId);
		storyModel.setSprint_id(unitTestProperties.modelSprintid);
		storyModel.setTechnology(unitTestProperties.modelTechnology);
		storyModel.setName(unitTestProperties.modelName);
		storyModel.setDescription(unitTestProperties.modelDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.modelAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.modelNotes);
		storyModel.setComments(unitTestProperties.modelComments);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.modelDueDate);
		storyModel.setPriority(unitTestProperties.modelPriority);
		storyModel.setAssignee_id(unitTestProperties.modelAssigneeId);
		storyModel.setHistory(historyList);
		storyModel.setTasks(getTaskModelList());

		return storyModel;
	}
	
	public static List<TaskModel> getTaskModelList(){
		List<TaskModel> tasks = new ArrayList<TaskModel>();
		TaskModel tasksModel = new TaskModel();
		tasksModel.set_id(unitTestProperties.TasksModelId);
		tasksModel.setName(unitTestProperties.TasksdomainName);
		tasksModel.setDescription(unitTestProperties.TasksdomainDescription);
		tasksModel.setStatus(unitTestProperties.TasksdomainStatus);
		tasksModel.setComments(unitTestProperties.TasksdomainComments);
		tasksModel.setAssignee(unitTestProperties.TasksdomainAssignee);
		tasks.add(tasksModel);
		return tasks;
	}
	
	public static StoryModel getStoryTaskModel() {
		StoryModel storyModel = new StoryModel();
		ArrayList<String> historyList = new ArrayList<>();
		ArrayList<TaskModel> taskList = new ArrayList<>();
		TaskModel taskModel = new TaskModel();
		taskModel.set_id("5e8cf37b7a605837de2865ad");
		taskModel.setName("New Tasks Armando");
		taskModel.setStatus("Working");
		taskModel.setAssignee("5e6bbc854244ac0cbc8df65d");
		taskList.add(taskModel);
		historyList.add(unitTestProperties.modelHistory1);
		historyList.add(unitTestProperties.modelHistory2);
		storyModel.set_id(unitTestProperties.modelId);
		storyModel.setSprint_id(unitTestProperties.modelSprintid);
		storyModel.setTechnology(unitTestProperties.modelTechnology);
		storyModel.setName(unitTestProperties.modelName);
		storyModel.setDescription(unitTestProperties.modelDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.modelAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.modelNotes);
		storyModel.setComments(unitTestProperties.modelComments);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.modelDueDate);
		storyModel.setPriority(unitTestProperties.modelPriority);
		storyModel.setAssignee_id(unitTestProperties.modelAssigneeId);
		storyModel.setHistory(historyList);
		storyModel.setTasks(taskList);
		
		return storyModel;
	}

	public static List<StoryModel> getStoryModelList() {
		List<StoryModel> storyModelList = new ArrayList<StoryModel>();
		StoryModel storyModel = new StoryModel();
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.modelHistory1);
		historyList.add(unitTestProperties.modelHistory2);
		storyModel.set_id(unitTestProperties.modelId);
		storyModel.setSprint_id(unitTestProperties.modelSprintid);
		storyModel.setTechnology(unitTestProperties.modelTechnology);
		storyModel.setName(unitTestProperties.modelName);
		storyModel.setDescription(unitTestProperties.modelDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.modelAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.modelNotes);
		storyModel.setComments(unitTestProperties.modelComments);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.modelDueDate);
		storyModel.setPriority(unitTestProperties.modelPriority);
		storyModel.setAssignee_id(unitTestProperties.modelAssigneeId);
		storyModel.setHistory(historyList);
		storyModelList.add(storyModel);
		return storyModelList;
	}

	public static List<SprintDomain> getSprintDomaintList() {
		List<SprintDomain> sprintDomainList = new ArrayList<SprintDomain>();
		SprintDomain sprintDomain = new SprintDomain();
		sprintDomain.setId("5e827f2f48b0866f87e1cbc2");
		sprintDomain.setName("asd");
		sprintDomain.setTechnology("asd");
		sprintDomain.setActive(true);
		sprintDomain.set_backlog(false);
		sprintDomain.setStart_date(LocalDate.now());
		sprintDomain.setEnd_date(LocalDate.now());
		sprintDomainList.add(sprintDomain);
		return sprintDomainList;
	}
	
	public static List<SprintDomain> getNullSprintDomaintList() {
		List<SprintDomain> sprintDomainList = new ArrayList<SprintDomain>();
		sprintDomainList.add(null);
		return sprintDomainList;
	}
	
	public static TasksDomain getDummyTasksDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id("5e8dc1ba4ce33c0efc555845");
		tasksDomain.setName("TaskDomainTest");
		tasksDomain.setDescription("TasksDomainDescriptionTest");
		tasksDomain.setStatus("TaskDomainStatusTest");
		tasksDomain.setComments("TasksDomainCommentsTest");
		tasksDomain.setAssignee("TasksDomainAssigneeTest");
		return tasksDomain;
	}
	
	public static TaskModel getDummyTaskModel() {
		TaskModel taskModel = new TaskModel();
		taskModel.set_id("5e8dc1ba4ce33c0efc555845");
		taskModel.setName("TaskModelTest");
		taskModel.setDescription("TasksModelDescriptionTest");
		taskModel.setStatus("TaskModelStatusTest");
		taskModel.setComments("TasksModelCommentsTest");
		taskModel.setAssignee("TasksModelAssigneeTest");
		return taskModel;
	}
	
	public static TasksDomain getUpdateTaskDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.setName("test");
		tasksDomain.setDescription("");
		tasksDomain.setStatus("Working");
		tasksDomain.setComments("comments");
		tasksDomain.setAssignee("");
		return tasksDomain;
	}
	
	public static TasksDomain getUpdateTaskDomainWrongstatus() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.setName("test");
		tasksDomain.setDescription("");
		tasksDomain.setStatus("Wrong");
		tasksDomain.setComments("comments");
		tasksDomain.setAssignee("");
		return tasksDomain;
	}
	
	public static TasksDomain getUpdateTaskDomainAssignee() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.setName("test");
		tasksDomain.setDescription("");
		tasksDomain.setStatus("Working");
		tasksDomain.setComments("comments");
		tasksDomain.setAssignee("5e6bbc854244ac0cbc8df65d");
		return tasksDomain;
	}
	
	public static TasksDomain getUpdateTaskDomainNameEmpty() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.setName(null);
		tasksDomain.setDescription("");
		tasksDomain.setStatus("Working");
		tasksDomain.setComments("comments");
		tasksDomain.setAssignee("5e6bbc854244ac0cbc8df65d");
		return tasksDomain;
  }
  
	public static TaskModel getTaskModelId() {
		TaskModel taskModel = new TaskModel();
		taskModel.set_id("5e7668cfacfc726352dc5abc");
		return taskModel;
	}
	
	public static TaskModel getTaskModelNull() {
		TaskModel taskModel = new TaskModel();
		taskModel.set_id(null);
		taskModel.setName("TaskModelTest");
		taskModel.setDescription("TasksModelDescriptionTest");
		taskModel.setStatus("TaskModelStatusTest");
		taskModel.setComments("TasksModelCommentsTest");
		taskModel.setAssignee("TasksModelAssigneeTest");
		return taskModel;
	}
	
	public static TasksDomain getTaskDomainSpecialChar() {
		TasksDomain taskDomain = new TasksDomain();
		taskDomain.set_id("5e8dc1ba4ce33c0efc555845");
		taskDomain.setName("TaskModelTest");
		taskDomain.setDescription("TasksModelDescriptionTest");
		taskDomain.setStatus("TaskModelStatusTest_");
		taskDomain.setComments("TasksModelCommentsTest");
		taskDomain.setAssignee("TasksModelAssigneeTest");
		return taskDomain;
	}
	
	public static TasksDomain getTaskDomainSpecialsChars() {
		TasksDomain taskDomain = new TasksDomain();
		taskDomain.set_id("5e8dc1ba4ce33c0efc555845");
		taskDomain.setName("TaskModelTest");
		taskDomain.setDescription("TasksModelDescriptionTest");
		taskDomain.setStatus("TaskModelStatusTest_");
		taskDomain.setComments("TasksModelCommentsTest");
		taskDomain.setAssignee("TasksModelAssigneeTest_");
		return taskDomain;
	}
	
	public static StoryDomain getStoryDomainSpecialsChars() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus+"_");
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId+"_");
		storyDomain.setHistory(historyList);

		return storyDomain;
	}
	
	public static StoryDomain getStoryDomainSpecialChar() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus+"_");
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);

		return storyDomain;
	}
	
	
	
	
	
	public static StoryDomain getStoryDomainAssigneInvalid() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelAssigneInvalid() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.domainPoints);
		storyModel.setProgress(unitTestProperties.domainProgress);
		storyModel.setStatus(unitTestProperties.domainStatus);
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.domainStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainNameInvalid() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName("");
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelNameInvalid() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName("");
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.domainPoints);
		storyModel.setProgress(unitTestProperties.domainProgress);
		storyModel.setStatus(unitTestProperties.domainStatus);
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.domainStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainStatusInvalid() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus+"a");
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelStatusInvalid() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.domainPoints);
		storyModel.setProgress(unitTestProperties.domainProgress);
		storyModel.setStatus(unitTestProperties.domainStatus+"a");
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.domainStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainStatusNull() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus("");
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelStatusNull() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.domainPoints);
		storyModel.setProgress(unitTestProperties.domainProgress);
		storyModel.setStatus("");
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.domainStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainStarDateNull() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(unitTestProperties.domainPoints);
		storyDomain.setProgress(unitTestProperties.domainProgress);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(null);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelStarDateNull() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(null);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainPointsProggresNegative() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(-1);
		storyDomain.setProgress(-1);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelPointsProggresNegative() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(-1);
		storyModel.setProgress(-1);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	public static StoryDomain getStoryDomainPointsProgressInvalid() {
		StoryDomain storyDomain = new StoryDomain();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyDomain.set_id(unitTestProperties.domainId);
		storyDomain.setSprint_id(unitTestProperties.domainSprintId);
		storyDomain.setTechnology(unitTestProperties.domainTechnology);
		storyDomain.setName(unitTestProperties.domainName);
		storyDomain.setDescription(unitTestProperties.domainDescription);
		storyDomain.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyDomain.setPoints(101);
		storyDomain.setProgress(101);
		storyDomain.setStatus(unitTestProperties.domainStatus);
		storyDomain.setNotes(unitTestProperties.domainNotes);
		storyDomain.setComments(unitTestProperties.domainComment);
		storyDomain.setStart_date(unitTestProperties.domainStartDate);
		storyDomain.setDue_date(unitTestProperties.domainDueDate);
		storyDomain.setPriority(unitTestProperties.domainPriority);
		storyDomain.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyDomain.setHistory(historyList);
		return storyDomain;
	}
	
	public static StoryModel getStoryModelPointsProgressInvalid() {
		StoryModel storyModel = new StoryModel();
		List<String> historyList = new ArrayList<>();
		historyList.add(unitTestProperties.domainHistory1);
		historyList.add(unitTestProperties.domainHistory2);
		storyModel.set_id(unitTestProperties.domainId);
		storyModel.setSprint_id(unitTestProperties.domainSprintId);
		storyModel.setTechnology(unitTestProperties.domainTechnology);
		storyModel.setName(unitTestProperties.domainName);
		storyModel.setDescription(unitTestProperties.domainDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.domainAcceptanceCriteria);
		storyModel.setPoints(101);
		storyModel.setProgress(101);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.domainNotes);
		storyModel.setComments(unitTestProperties.domainComment);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.domainDueDate);
		storyModel.setPriority(unitTestProperties.domainPriority);
		storyModel.setAssignee_id(unitTestProperties.domainAssigneeId);
		storyModel.setHistory(historyList);
		return storyModel;
	}
	
	
	public static StoryModel getStoryTaskNullModel() {
		StoryModel storyModel = new StoryModel();
		ArrayList<String> historyList = new ArrayList<>();
		ArrayList<TaskModel> taskList = new ArrayList<>();
		TaskModel taskModel = new TaskModel();
		historyList.add(unitTestProperties.modelHistory1);
		historyList.add(unitTestProperties.modelHistory2);
		storyModel.set_id(unitTestProperties.modelId);
		storyModel.setSprint_id(unitTestProperties.modelSprintid);
		storyModel.setTechnology(unitTestProperties.modelTechnology);
		storyModel.setName(unitTestProperties.modelName);
		storyModel.setDescription(unitTestProperties.modelDescription);
		storyModel.setAcceptance_criteria(unitTestProperties.modelAcceptanceCriteria);
		storyModel.setPoints(unitTestProperties.modelPoints);
		storyModel.setProgress(unitTestProperties.modelProgress);
		storyModel.setStatus(unitTestProperties.modelStatus);
		storyModel.setNotes(unitTestProperties.modelNotes);
		storyModel.setComments(unitTestProperties.modelComments);
		storyModel.setStart_date(unitTestProperties.modelStartDate);
		storyModel.setDue_date(unitTestProperties.modelDueDate);
		storyModel.setPriority(unitTestProperties.modelPriority);
		storyModel.setAssignee_id(unitTestProperties.modelAssigneeId);
		storyModel.setHistory(historyList);
		storyModel.setTasks(taskList);
		
		return storyModel;
	}
	
	public static TasksDomain getTasksDomainStatusValid() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id(unitTestProperties.TasksdomainId);
		tasksDomain.setName(unitTestProperties.TasksdomainName);
		tasksDomain.setDescription(unitTestProperties.TasksdomainDescription);
		tasksDomain.setStatus(unitTestProperties.TasksdomainStatus + "asd");
		tasksDomain.setComments(unitTestProperties.TasksdomainComments);
		tasksDomain.setAssignee("");
		return tasksDomain;
	}
	
	public static TasksDomain getTasksNameNullDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id(unitTestProperties.TasksdomainId);
		tasksDomain.setName("");
		tasksDomain.setDescription(unitTestProperties.TasksdomainDescription);
		tasksDomain.setStatus(unitTestProperties.TasksdomainStatus);
		tasksDomain.setComments(unitTestProperties.TasksdomainComments);
		tasksDomain.setAssignee(unitTestProperties.TasksdomainAssignee);
		return tasksDomain;
	}
	
	public static TasksDomain getTasksStatusNullDomain() {
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id(unitTestProperties.TasksdomainId);
		tasksDomain.setName(unitTestProperties.TasksdomainName);
		tasksDomain.setDescription(unitTestProperties.TasksdomainDescription);
		tasksDomain.setStatus("");
		tasksDomain.setComments(unitTestProperties.TasksdomainComments);
		tasksDomain.setAssignee(unitTestProperties.TasksdomainAssignee);
		return tasksDomain;
	}
}
