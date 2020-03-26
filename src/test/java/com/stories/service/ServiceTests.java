package com.stories.service;

//import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;

import ma.glasnost.orika.MapperFacade;

@RunWith(SpringRunner.class)
public class ServiceTests {

	@MockBean
	StoriesRepository storiesRepository;

	@MockBean
	private MapperFacade mapperFacade;

	@MockBean
	SprintsClient sprintsClient;

	@InjectMocks
	StoriesServiceImpl storiesServiceImpl;

	StoryModel storyModel = new StoryModel();
	StoryDomain storyDomain = new StoryDomain();
	String id = "5e737810acfc726352dc5aba";
	String sprintId = "5e78f5e792675632e42d1a96";
	List<StoryModel> storiesModel = new ArrayList<StoryModel>();

	private EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Story not found",
			StoryDomain.class);

	private EntityNotFoundException entityNotFoundExceptionSprints = new EntityNotFoundException(
			"The sprint is not exists", SprintsClient.class);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getgetById() throws Exception {
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(storiesRepository.findById(id)).thenReturn(java.util.Optional.of(TestUtils.getDummyStoryModel()));
		when(mapperFacade.map(storyModel, StoryDomain.class)).thenReturn(TestUtils.getDummyStoryDoamin());
		when(storiesServiceImpl.getStoryById(id)).thenReturn(TestUtils.getDummyStoryDoamin());
		assertEquals(TestUtils.getDummyStoryDoamin(), storiesServiceImpl.getStoryById(id));
	}

	@Test(expected = EntityNotFoundException.class)
	public void getgetByIdException() throws Exception {
		when(!storiesRepository.existsById(id)).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.getStoryById(id)).thenThrow(entityNotFoundException);
	}

	@Test
	public void getAllStories() throws Exception {
		when(storiesRepository.findAll()).thenReturn(storiesModel);
		assertEquals(storiesModel, storiesServiceImpl.getAllStories());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getAllStoriesException() throws Exception {
		when(storiesRepository.findAll()).thenReturn(TestUtils.listStoriesModelNull());
		Mockito.when(storiesServiceImpl.getAllStories()).thenThrow(entityNotFoundException);
	}

	@Ignore
	@Test
	public void updateStory() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDoamin(), StoryModel.class))
				.thenReturn(TestUtils.getDummyStoryModel());
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(TestUtils.getDummyStoryModel());
		when(mapperFacade.map(storyDomain, StoryDomain.class)).thenReturn(TestUtils.getDummyStoryDoamin());
		assertEquals(TestUtils.getDummyStoryDoamin(),
				storiesServiceImpl.updateStory(TestUtils.getDummyStoryDoamin(), id));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryException() throws Exception {
		when(sprintsClient.existsSprintById(sprintId + "S")).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.updateStory(getStoryDomain(), id)).thenThrow(entityNotFoundExceptionSprints);
		storiesServiceImpl.updateStory(getStoryDomain(), id);
	}

	@Test
	public void deleteStory() throws Exception {
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		Mockito.doNothing().when(storiesRepository).deleteById(id);
		storiesServiceImpl.deleteStory(id);
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteStoryException() throws Exception {
		when(storiesRepository.existsById(id)).thenReturn(Boolean.FALSE);
		storiesServiceImpl.deleteStory(id);
	}

	@Ignore
	@Test
	public void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDoamin(), StoryModel.class)).thenReturn(storyModel);
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(storyModel);
		assertEquals(id, storiesServiceImpl.createStory(TestUtils.getDummyStoryDoamin()));
	}

	@Ignore
	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		when(mapperFacade.map(getStoryDomain(), StoryModel.class)).thenReturn(getStoryModel(id));
		Mockito.doReturn(Boolean.FALSE).when(sprintsClient).existsSprintById(sprintId);
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