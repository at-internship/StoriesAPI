package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.model.TaskModel;
import com.stories.repository.StoriesCustomRepository;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;
import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

import ma.glasnost.orika.MapperFacade;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class ServiceTests {

	@MockBean
	StoriesRepository storiesRepository;

	@MockBean
	UsersRepository usersRepository;
	
	@MockBean
	StoriesCustomRepository storiesCustomRepository;

	@MockBean
	private MapperFacade mapperFacade;

	@Autowired
	UnitTestProperties unitTestProperties;

	@MockBean
	SprintsClient sprintsClient;

	@InjectMocks
	StoriesServiceImpl storiesServiceImpl;

	private TestUtils testUtils;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

    MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
    
	@Test
	public void getById() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.TRUE);
		when(storiesRepository.findById(unitTestProperties.getUrlId()))
				.thenReturn(java.util.Optional.of(TestUtils.getDummyStoryModel()));
		when(mapperFacade.map(testUtils.getStoryModel(), StoryDomain.class))
				.thenReturn(testUtils.getDummyStoryDomain());
		when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenReturn(testUtils.getDummyStoryDomain());
		assertEquals(testUtils.getDummyStoryDomain(), storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenThrow(new EntityNotFoundException("Story not found", "/stories/"));
	}

	@Test
	public void getAllStories() throws Exception {
		when(storiesRepository.findAll()).thenReturn(storiesServiceImpl.storiesModel);
		assertEquals(storiesServiceImpl.storiesDomain, storiesServiceImpl.getAllStories());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getAllStoriesException() throws Exception {
		when(storiesRepository.findAll()).thenReturn(TestUtils.listStoriesModelNull());
		Mockito.when(storiesServiceImpl.getAllStories())
				.thenThrow(new EntityNotFoundException("Stories not found", "/stories/"));
	}

	@Test
	public void updateStory() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(true);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(true);
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(true);
		when(mapperFacade.map(testUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getModelId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateUserException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(false);
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStorySprintException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(true);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(false);
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryIdException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(true);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(true);
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(false);
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryStatusException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(true);
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyModel.get_id()))
				.thenThrow(new EntityNotFoundException(
						"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
						"", "/stories/"));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateException() throws Exception {
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, testUtils.getStoryModel().get_id()))
				.thenThrow(new EntityNotFoundException("Story not found", "/stories/"));
	}

	@Test
	public void deleteStory() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.TRUE);
		Mockito.doNothing().when(storiesRepository).deleteById(unitTestProperties.getUrlId());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteStoryException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.FALSE);
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}
	
	@Test
	public void deleteTask() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		Mockito.when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(Boolean.TRUE);
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		when(storiesRepository.findById(storiesServiceImpl.storyModel.get_id()).get()).thenReturn(storiesServiceImpl.storyModel);
		Mockito.doNothing().when(storiesRepository.save(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id());
		
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.FALSE);
		storiesServiceImpl.deleteTask(unitTestProperties.getUrlId(), null);
	}

	@Test
	public void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(true);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(true);
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(testUtils.getStoryModel());
		assertEquals(unitTestProperties.getUrlId(), storiesServiceImpl.createStory(TestUtils.getStoryDomain()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
				"", "/stories/"));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStorySprintIdException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setSprint_id("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setSprint_id("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain))
				.thenThrow(new EntityNotFoundException("The sprint_id does not exists", "/sprints/"));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryNameInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setName("");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setName("");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required field ('Name').", "", "/stories/"));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryStatusInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required field ('Name').", "", "/stories/"));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStartDateNull() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStart_date(null);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStart_date(null);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required field ('Name').", "", "/stories/"));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createPointsProggresNegative() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setPoints(-1);
		storiesServiceImpl.storyDomain.setProgress(-1);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setPoints(-1);
		storiesServiceImpl.storyModel.setProgress(-1);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required field ('Name').", "", "/stories/"));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createPointsProgressInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setPoints(123);
		storiesServiceImpl.storyDomain.setProgress(123);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setPoints(123);
		storiesServiceImpl.storyModel.setProgress(123);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(true);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required field ('Name').", "", "/stories/"));
	}
	
	@Test 
	public void getTasksByStory() throws Exception {
		AggregationResults <TasksDomain> aggregationResultsMock = Mockito.mock(AggregationResults.class);
        Aggregation aggregateMock = Mockito.mock(Aggregation.class);
        
        when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(true);
        Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TasksDomain.class));        
        Mockito.doReturn(testUtils.getTasksDomainList()).when(aggregationResultsMock).getMappedResults();        
        when(storiesCustomRepository.getTasksByStory(unitTestProperties.getUrlId())).thenReturn(aggregationResultsMock);        
       when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
       .thenReturn(testUtils.getTasksDomainList());
        
    assertEquals(testUtils.getTasksDomainList(), storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTasksByStoryFail() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
        .thenReturn(false);
		
		Mockito.when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
        .thenThrow(new EntityNotFoundException("Story not found", "/stories/" + unitTestProperties.getUrlId()));
		
	}
	
	@Test
	public void getTaskById() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		TaskModel taskModel = new TaskModel();
		taskModel.set_id("5e8dc1ba4ce33c0efc555845");
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id("5e8dc1ba4ce33c0efc555845");
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
			.thenReturn(Boolean.TRUE);
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(taskModel).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
			.thenReturn(testUtils.getDummyTasksDomain());
		when(storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(testUtils.getDummyTasksDomain());
		assertEquals(testUtils.getDummyTasksDomain(), storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdNoStory() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
		.thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenThrow(new EntityNotFoundException("Story not found", "/stories/" + unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdTry() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		TaskModel taskModel = new TaskModel();
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id("5e8dc1ba4ce33c0efc555845");
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
			.thenReturn(Boolean.TRUE);
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(taskModel).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
			.thenReturn(testUtils.getDummyTasksDomain());
		when(storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(testUtils.getDummyTasksDomain());
		assertEquals(testUtils.getDummyTasksDomain(), storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}
}