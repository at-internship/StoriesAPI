package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	private StoriesApiConstants storiesApiConstants;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
		storiesApiConstants = new StoriesApiConstants();
	}

    MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
    
	@Test
	public void getById() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanTrue());
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
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath()));
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
				.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStories(),storiesApiConstants.getPath()));
	}

	@Test
	public void updateStory() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(testUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getModelId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateUserException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStorySprintException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryIdException() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryStatusException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyModel.get_id()))
				.thenThrow(new EntityNotFoundException(
						storiesApiConstants.getMessageStatusInvalid(),
						storiesApiConstants.getVarEmpty(), 
						storiesApiConstants.getPath()));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateException() throws Exception {
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, testUtils.getStoryModel().get_id()))
				.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath()));
	}

	@Test
	public void deleteStory() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doNothing().when(storiesRepository).deleteById(unitTestProperties.getUrlId());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteStoryException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}
	
	@Test
	public void deleteTask() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		Mockito.when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(unitTestProperties.getModelId())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskNotFoundException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		Mockito.when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(unitTestProperties.getModelId())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()+storiesApiConstants.getPlusError());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.deleteTask(unitTestProperties.getUrlId(), storiesServiceImpl.storyModel.get_id());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskNullException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		List<TaskModel> task = new ArrayList<>();
		storiesServiceImpl.storyModel.setTasks(task);
		Mockito.when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(unitTestProperties.getModelId())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesApiConstants.getVarEmpty());
	}
	
	@Test
	public void createTask() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		taskDomain.setName("Taks");
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(usersRepository.existsById(taskDomain.getAssignee())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(unitTestProperties.getModelId()))
		.thenReturn(java.util.Optional.of(TestUtils.getStoryModel()));
		when(mapperFacade.map(taskDomain, TaskModel.class)).thenReturn(TestUtils.getTasksModel());
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(TestUtils.getStoryModel());
		storiesServiceImpl.createTask(taskDomain, unitTestProperties.getModelId());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskStoryExistException() throws Exception{
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.createTask(TestUtils.getTasksDomain(), unitTestProperties.getModelId())).
			thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), HttpStatus.CONFLICT,storiesApiConstants.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskNameException() throws Exception{
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesServiceImpl.createTask(TestUtils.getTasksDomain(), unitTestProperties.getModelId())).
		thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageName(),storiesApiConstants.getNumbreError(),storiesApiConstants.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskUserExistException() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		taskDomain.setName("Taks");
		taskDomain.setAssignee("ss");
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(usersRepository.existsById(TestUtils.getDummyTasksDomain().getAssignee())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.createTask(taskDomain, unitTestProperties.getModelId())).
			thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), HttpStatus.CONFLICT,storiesApiConstants.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskStatusException() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		taskDomain.setName("Taks");
		taskDomain.setStatus("error");
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(usersRepository.existsById(TestUtils.getDummyTasksDomain().getAssignee())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesServiceImpl.createTask(taskDomain, unitTestProperties.getModelId())).
			thenThrow(new EntityNotFoundException(
					storiesApiConstants.getMessageStatusInvalid(),
					storiesApiConstants.getNumbreError(),
					storiesApiConstants.getPath()));
	}

	@Test
	public void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(testUtils.getStoryModel());
		assertEquals(unitTestProperties.getUrlId(), storiesServiceImpl.createStory(TestUtils.getStoryDomain()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageStatusInvalid(),
				storiesApiConstants.getVarEmpty(), 
				storiesApiConstants.getPath()));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStorySprintIdException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setSprint_id("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setSprint_id("incorrect");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain))
				.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageSprintId(), storiesApiConstants.getPath()));
		storiesServiceImpl.createStory(storiesServiceImpl.storyDomain);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryNameInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setName("");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setName("");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageName(),
				storiesApiConstants.getVarEmpty(),
				storiesApiConstants.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryStatusInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("");
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageName(), 
				storiesApiConstants.getVarEmpty(), 
				storiesApiConstants.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStartDateNull() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStart_date(null);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStart_date(null);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageName(), 
				storiesApiConstants.getVarEmpty(), 
				storiesApiConstants.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createPointsProggresNegative() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setPoints(-1);
		storiesServiceImpl.storyDomain.setProgress(-1);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setPoints(-1);
		storiesServiceImpl.storyModel.setProgress(-1);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageName(), 
				storiesApiConstants.getVarEmpty(), 
				storiesApiConstants.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createPointsProgressInvalid() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setPoints(123);
		storiesServiceImpl.storyDomain.setProgress(123);
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setPoints(123);
		storiesServiceImpl.storyModel.setProgress(123);
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(storiesServiceImpl.storyDomain.getSprint_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class))
				.thenReturn(storiesServiceImpl.storyModel);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(
				storiesApiConstants.getMessageName(), 
				storiesApiConstants.getVarEmpty(), 
				storiesApiConstants.getPath()));
	}
	
	@Test 
	public void getTasksByStory() throws Exception {
		AggregationResults <TasksDomain> aggregationResultsMock = Mockito.mock(AggregationResults.class);
        Aggregation aggregateMock = Mockito.mock(Aggregation.class);
        
        when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanTrue());
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
        .thenReturn(storiesApiConstants.getBooleanFalse());
		
		Mockito.when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
        .thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath() + unitTestProperties.getUrlId()));
		
	}
	
	@Test
	public void getTaskById() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
			.thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(testUtils.getTaskModelId()).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(aggregationResultsMock);	
		when(mapperFacade.map(testUtils.getTaskModelId(), TasksDomain.class))
			.thenReturn(testUtils.getDummyTasksDomain());
		when(storiesRepository.findByTasks__id(unitTestProperties.getUrlId()))
			.thenReturn(testUtils.getTaskModelId());
		assertEquals(testUtils.getDummyTasksDomain(), storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdNoStory() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
		.thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath() + unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdNoEquals() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
			.thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(testUtils.getTaskModelId()).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(aggregationResultsMock);	
		when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
			.thenReturn(testUtils.getDummyTasksDomain());
		when(storiesRepository.findByTasks__id(unitTestProperties.getUrlId()))
			.thenReturn(testUtils.getDummyTaskModel());
		assertEquals(storiesApiConstants.getSpecificId(), testUtils.getDummyTaskModel().get_id());
		assertEquals(testUtils.getDummyTasksDomain(), storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdTry() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
			.thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(testUtils.getTaskModelNull()).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
			.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
			.thenReturn(testUtils.getDummyTasksDomain());
		assertEquals(testUtils.getDummyTasksDomain(), storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}
}