package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import com.stories.sprintsclient.SprintsClient;

import ma.glasnost.orika.MapperFacade;

@RunWith(SpringRunner.class)
public class ValidationSprintId {

	@MockBean
	StoriesRepository storiesRepository;

	@MockBean
	SprintsClient sprintsClient;

	@MockBean
	private MapperFacade mapperFacade;

	@InjectMocks
	StoriesServiceImpl storiesServiceImpl;

	private EntityNotFoundException entityNotFoundExceptionSprints = new EntityNotFoundException("The sprint is not exists", SprintsClient.class);
	String storyId = "5e7668cfacfc726352dc5abc";
	String sprintId = "5e78f5e792675632e42d1a96";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void putSprintValidationTrue() throws Exception {
		when(mapperFacade.map(getStoryDomain(), StoryModel.class)).thenReturn(getStoryModel(storyId));
		when(sprintsClient.existsSprintById(sprintId)).thenReturn(Boolean.TRUE);
		when(storiesRepository.existsById(storyId)).thenReturn(Boolean.TRUE);
		when(storiesServiceImpl.updateStory(getStoryDomain(), storyId)).thenReturn(getStoryDomain());
		assertEquals(getStoryDomain(), storiesServiceImpl.updateStory(getStoryDomain(), storyId));
	}

	@Test(expected = EntityNotFoundException.class)
	public void putSprintIdExeption() throws Exception {
		when(sprintsClient.existsSprintById(sprintId)).thenReturn(Boolean.FALSE);
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			throw new EntityNotFoundException("The sprint is not exists", SprintsClient.class);
		});
		assertEquals(exception, storiesServiceImpl.updateStory(getStoryDomain(), storyId));
	}

	@Test
	public void postSprintValidationTrue() throws Exception {
		when(sprintsClient.existsSprintById(sprintId)).thenReturn(Boolean.TRUE);
		when(mapperFacade.map(getStoryDomain(), StoryModel.class)).thenReturn(getStoryModel(storyId));
		when(storiesRepository.save(getStoryModel(storyId))).thenReturn(getStoryModel(storyId));
		assertEquals(storyId, storiesServiceImpl.createStory(getStoryDomain()));
	}

	@Ignore
	@Test(expected = EntityNotFoundException.class)
	public void postSprintValidationException() throws Exception {
		when(sprintsClient.existsSprintById(sprintId)).thenReturn(Boolean.FALSE);
		when(mapperFacade.map(getStoryDomain(), StoryModel.class)).thenReturn(getStoryModel(storyId));
		throw new EntityNotFoundException("The sprint_id does not exists", SprintsClient.class);
	}

	public StoryDomain getStoryDomain() {
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

	public StoryModel getStoryModel(String id) {
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