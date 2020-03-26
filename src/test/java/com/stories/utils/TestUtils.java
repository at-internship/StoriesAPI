package com.stories.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;

public class TestUtils {
	
	public static String storyId = "5e7668cfacfc726352dc5abc";
	public static String sprintId = "5e78f5e792675632e42d1a96";

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
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\"%,\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	public String getByid() {
		return "{\"sprint_id\":\"hola\", \"technology\":\"Java\",\"name\":\"Probando la impresion en consola y json file\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":1,\"progress\":1, \"status\":\"Working\",\"notes\":\"!\",\"comments\":\"$\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"%\", \"assignee_id\":\"Prueba\",\"history\":[\"1\",\"2\"]}";
	}

	public static StoryDomain getDummyStoryDoamin() {
		
		LocalDate date = LocalDate.now();
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add("1");
		historyList.add("2");
		StoryDomain storyDomain = new StoryDomain();
		storyDomain.setSprint_id("hola");
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

		LocalDate date = LocalDate.now();
		ArrayList<String> historyList = new ArrayList<>();
		historyList.add("1");
		historyList.add("2");
		StoryModel storyModel = new StoryModel();
		storyModel.set_id("5e737810acfc726352dc4abc");
		storyModel.setSprint_id("For the Test Post");
		storyModel.setTechnology("Java");
		storyModel.setName("Try Test");
		storyModel.setDescription("");
		storyModel.setAcceptance_criteria("");
		storyModel.setPoints(1);
		storyModel.setProgress(1);
		storyModel.setStatus("Working");
		storyModel.setNotes("!");
		storyModel.setComments("$");
		storyModel.setStart_date(date);
		storyModel.setDue_date(date);
		storyModel.setPriority("%");
		storyModel.setAssignee_id("Try Test");
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
		LocalDate date = LocalDate.now();
		List<String> historyList = new ArrayList<>();
		historyList.add("1");
		historyList.add("2");
		storyDomain.setSprint_id(sprintId);
		storyDomain.setTechnology("Javas");
		storyDomain.setName("Create Stories POST endpoint");
		storyDomain.setDescription("");
		storyDomain.setAcceptance_criteria("");
		storyDomain.setPoints(1);
		storyDomain.setProgress(2);
		storyDomain.setStatus("Working");
		storyDomain.setNotes("");
		storyDomain.setComments("Test");
		storyDomain.setStart_date(date);
		storyDomain.setDue_date(date);
		storyDomain.setPriority("High");
		storyDomain.setAssignee_id("UUID");
		storyDomain.setHistory(historyList);

		return storyDomain;
	}

	public static StoryModel getStoryModel(String id) {
		StoryModel storyModel = new StoryModel();
		LocalDate localDate = LocalDate.now();
		List<String> histories = new ArrayList<>();
		histories.add("1");
		histories.add("2");
		storyModel.set_id(id);
		storyModel.setSprint_id(null);
		storyModel.setTechnology("Javas");
		storyModel.setName("Create Stories POST endpoint");
		storyModel.setDescription("");
		storyModel.setAcceptance_criteria("");
		storyModel.setPoints(1);
		storyModel.setProgress(2);
		storyModel.setStatus("Working");
		storyModel.setNotes("");
		storyModel.setComments("Test");
		storyModel.setStart_date(localDate);
		storyModel.setDue_date(localDate);
		storyModel.setPriority("High");
		storyModel.setAssignee_id("UUID");
		storyModel.setHistory(histories);

		return storyModel;
	}
}
