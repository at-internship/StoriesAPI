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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
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
import com.stories.validations.DynamicValidation;
import com.stories.validations.DynamicValidationArray;

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
	
	@MockBean
	DynamicValidation dynamicValidation;
	
	@InjectMocks
	StoriesServiceImpl storiesServiceImpl;
	
	private TestUtils testUtils;
	private StoriesApiConstants storiesApiConstants;
	private DynamicValidationArray dynamicValidationArray;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
		dynamicValidationArray = new DynamicValidationArray();
		storiesApiConstants = new StoriesApiConstants();
	}

    MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);
    
	@Test
	public void getById() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyDomain.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyDomain.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(storiesServiceImpl.storyDomain.get_id()))
				.thenReturn(Optional.of(storiesServiceImpl.storyModel));
		when(mapperFacade.map(storiesServiceImpl.storyModel, StoryDomain.class))
				.thenReturn(storiesServiceImpl.storyDomain);
		when(storiesServiceImpl.getStoryById(storiesServiceImpl.storyDomain.get_id()))
				.thenReturn(storiesServiceImpl.storyDomain);
		assertEquals(storiesServiceImpl.storyDomain, storiesServiceImpl.getStoryById(storiesServiceImpl.storyDomain.get_id()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyDomain.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyDomain.get_id())).thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getStoryById(storiesServiceImpl.storyDomain.get_id()))
				.thenThrow(new EntityNotFoundException("Story not found", "/stories/"));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getByIdCharactersException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyDomain.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyDomain.get_id())).thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getStoryById(storiesServiceImpl.storyDomain.get_id()))
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
				.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStories(),storiesApiConstants.getPath()));
	}

	@Test
	public void updateStory() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		List<TaskModel> tasksModel = TestUtils.getListTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).thenReturn(dynamicValidationArray);
		when(storiesServiceImpl.storiesRepository.findById(storiesServiceImpl.storyDomain.get_id())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		when(usersRepository.existsById(storiesServiceImpl.storyDomain.getAssignee_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.existsById(storiesServiceImpl.storyDomain.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class)).thenReturn(storiesServiceImpl.storyModel);
		storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id());
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateUserException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayConflict();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).thenReturn(dynamicValidationArray);
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).
		thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), HttpStatus.CONFLICT,
				dynamicValidationArray.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryIdException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).thenReturn(dynamicValidationArray);
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).
		thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
				dynamicValidationArray.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryStatusException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).thenReturn(dynamicValidationArray);
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesRepository.existsById(unitTestProperties.getModelId())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, storiesServiceImpl.storyDomain.get_id())).
		thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
				dynamicValidationArray.getPath()));
	}

	@Test
	public void deleteStory() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), "")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doNothing().when(storiesRepository).deleteById(unitTestProperties.getUrlId());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteStoryException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), "")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteStoryCharactersException() throws Exception {
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), "")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}
	
	@Test
	public void deleteTask() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		Mockito.when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(storiesServiceImpl.storyModel.get_id())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskNotFoundException() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id() + "asd")).thenReturn(dynamicValidationArray);
		Mockito.when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(storiesServiceImpl.storyModel.get_id())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id() + "asd");
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskException() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanFalse());
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskNullException() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		List<TaskModel> task = new ArrayList<>();
		storiesServiceImpl.storyModel.setTasks(task);
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
		Mockito.when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(storiesServiceImpl.storyModel.get_id())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesApiConstants.getVarEmpty());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteTaskCharactersException() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		List<TaskModel> task = new ArrayList<>();
		storiesServiceImpl.storyModel.setTasks(task);
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
		Mockito.when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(storiesServiceImpl.storyModel.get_id())).thenReturn(Optional.of(storiesServiceImpl.storyModel));
		storiesServiceImpl.deleteTask(storiesServiceImpl.storyModel.get_id(), storiesApiConstants.getVarEmpty());
	}
	
	@Test
	public void createTask() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		TaskModel taskModel = TestUtils.getTasksModel();
		taskDomain.setName("Taks");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.taskValidation(taskDomain, storiesServiceImpl.storyModel.get_id(),"")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(usersRepository.existsById(taskDomain.getAssignee())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesRepository.findById(unitTestProperties.getModelId()))
		.thenReturn(Optional.of(storiesServiceImpl.storyModel));
		when(mapperFacade.map(taskDomain, TaskModel.class)).thenReturn(taskModel);
		when(storiesRepository.save(storiesServiceImpl.storyModel)).thenReturn(storiesServiceImpl.storyModel);
		storiesServiceImpl.createTask(taskDomain, storiesServiceImpl.storyModel.get_id());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskStoryExistException() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.taskValidation(taskDomain, storiesServiceImpl.storyModel.get_id(),"")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanFalse());
		when(storiesServiceImpl.createTask(taskDomain, storiesServiceImpl.storyModel.get_id())).
			thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), HttpStatus.CONFLICT,storiesApiConstants.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskNameException() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.taskValidation(taskDomain, storiesServiceImpl.storyModel.get_id(),"")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesServiceImpl.createTask(taskDomain, storiesServiceImpl.storyModel.get_id())).
			thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
					dynamicValidationArray.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void createTaskUserExistException() throws Exception{
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayConflict();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.taskValidation(taskDomain, storiesServiceImpl.storyModel.get_id(),"")).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
		when(storiesServiceImpl.createTask(taskDomain, storiesServiceImpl.storyModel.get_id())).
			thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
					dynamicValidationArray.getPath()));
	}
	
	@Test
	public void createStory() throws Exception {
		String validation="";
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setName("validation");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getStart_date())).thenReturn(storiesServiceImpl.storyDomain.getStart_date());
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getDue_date())).thenReturn(storiesServiceImpl.storyDomain.getDue_date());
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, validation)).thenReturn(dynamicValidationArray);
		when(mapperFacade.map(storiesServiceImpl.storyDomain, StoryModel.class)).thenReturn(storiesServiceImpl.storyModel);
		when(storiesRepository.save(storiesServiceImpl.storyModel)).thenReturn(storiesServiceImpl.storyModel);
		when(dynamicValidation.nameValidation(storiesServiceImpl.storyModel)).thenReturn(storiesServiceImpl.storyModel);
		
		assertEquals(unitTestProperties.getModelId(), storiesServiceImpl.createStory(storiesServiceImpl.storyDomain));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		String validation="";
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getStart_date())).thenReturn(storiesServiceImpl.storyDomain.getStart_date());
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getDue_date())).thenReturn(storiesServiceImpl.storyDomain.getDue_date());
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, validation)).thenReturn(dynamicValidationArray);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
				dynamicValidationArray.getPath()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStorySprintIdException() throws Exception {
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setSprint_id("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setSprint_id("incorrect");
		
		String validation="";
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		dynamicValidationArray = TestUtils.getDynamicArrayConflict();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getStart_date())).thenReturn(storiesServiceImpl.storyDomain.getStart_date());
		when(dynamicValidation.startDate(storiesServiceImpl.storyDomain.getDue_date())).thenReturn(storiesServiceImpl.storyDomain.getDue_date());
		when(dynamicValidation.storyValidation(storiesServiceImpl.storyDomain, validation)).thenReturn(dynamicValidationArray);
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain)).thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
				dynamicValidationArray.getPath()));
	}

	@Test 
	public void getTasksByStory() throws Exception {
		String validation="";
		List<TasksDomain> listTasks = TestUtils.getTasksDomainList();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		AggregationResults <TasksDomain> aggregationResultsMock = Mockito.mock(AggregationResults.class);
        Aggregation aggregateMock = Mockito.mock(Aggregation.class);
        
        when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
        when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id())).thenReturn(storiesApiConstants.getBooleanTrue());
        Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TasksDomain.class));        
        Mockito.doReturn(listTasks).when(aggregationResultsMock).getMappedResults();        
        when(storiesCustomRepository.getTasksByStory(storiesServiceImpl.storyModel.get_id())).thenReturn(aggregationResultsMock);        
       when(storiesServiceImpl.getTasksByStory(storiesServiceImpl.storyModel.get_id()))
       .thenReturn(listTasks);
        
    assertEquals(listTasks, storiesServiceImpl.getTasksByStory(storiesServiceImpl.storyModel.get_id()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTasksByStoryFail() throws Exception {
		String validation="";
		List<TasksDomain> listTasks = TestUtils.getTasksDomainList();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
        .thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
        .thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath() + unitTestProperties.getUrlId()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTasksByStoryCharacters() throws Exception {
		String validation="";
		List<TasksDomain> listTasks = TestUtils.getTasksDomainList();
		storiesServiceImpl.storyDomain = TestUtils.getStoryDomain();
		storiesServiceImpl.storyDomain.setStatus("incorrect");
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		storiesServiceImpl.storyModel.setStatus("incorrect");
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
        .thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
        .thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
				dynamicValidationArray.getPath()));
	}
	
	@Test
	public void getTaskById() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		dynamicValidationArray = TestUtils.getDynamicArray();
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		TaskModel taskModel = new TaskModel();
		taskModel.set_id("5e8dc1ba4ce33c0efc555845");
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id("5e8dc1ba4ce33c0efc555845");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id()))
			.thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(taskModel).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()))
			.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(taskModel, TasksDomain.class))
			.thenReturn(tasksDomain);
		when(storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()))
			.thenReturn(tasksDomain);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdNoStory() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id()))
		.thenReturn(storiesApiConstants.getBooleanFalse());
		Mockito.when(storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()))
			.thenThrow(new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath() + storiesServiceImpl.storyModel.get_id()));
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
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArray();
		
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		TaskModel taskModel = new TaskModel();
		TasksDomain tasksDomain = new TasksDomain();
		tasksDomain.set_id("5e8dc1ba4ce33c0efc555845");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), validation)).thenReturn(dynamicValidationArray);
		when(storiesRepository.existsById(storiesServiceImpl.storyModel.get_id()))
			.thenReturn(storiesApiConstants.getBooleanTrue());
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate)
			.aggregate(Mockito.any(Aggregation.class), Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(taskModel).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(storiesServiceImpl.storyModel.get_id(), validation))
			.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(storiesServiceImpl.storyModel, TasksDomain.class))
			.thenReturn(tasksDomain);
		when(storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), validation))
			.thenReturn(tasksDomain);
		assertEquals(tasksDomain, storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), validation));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdCharacters() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		Mockito.when(storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()))
			.thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
					dynamicValidationArray.getPath()));
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdUser() throws Exception {
		TasksDomain taskDomain = TestUtils.getTasksDomain();
		storiesServiceImpl.storyModel = TestUtils.getStoryTaskModel();
		String validation="";
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("asd");
		
		when(dynamicValidation.idValidation(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id())).thenReturn(dynamicValidationArray);
		Mockito.when(storiesServiceImpl.getTaskById(storiesServiceImpl.storyModel.get_id(), storiesServiceImpl.storyModel.getTasks().get(0).get_id()))
			.thenThrow(new EntityNotFoundException(dynamicValidationArray.getMessage().toString(), "",
					dynamicValidationArray.getPath()));
	}
}