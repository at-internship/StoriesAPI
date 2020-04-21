package com.stories.validations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stories.constants.StoriesApiConstants;
import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesCustomRepository;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;
import com.stories.service.StoriesServiceImpl;
import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

import ma.glasnost.orika.MapperFacade;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
public class ValidationsTests {

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
	SprintsClient sprintClient;
	
	@InjectMocks
	DynamicValidation dynamicValidation;
	
	private TestUtils testUtils;
	private StoryDomain storyDomain;
	private StoryModel storyModel;
	private TasksDomain task;
	private DynamicValidationArray dynamicValidationArray;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
		storyDomain = new StoryDomain();
		storyModel = new StoryModel();
		task = new TasksDomain();
		dynamicValidationArray = new DynamicValidationArray();
	}
	
	@Test
	public void nameValidation() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		when(dynamicValidation.nameValidation(storyModel)).thenReturn(storyModel);
	}
	
	@Test
	public void startDateDueDate() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		assertEquals(LocalDate.now(), dynamicValidation.startDate(LocalDate.now()));
		assertEquals(LocalDate.now(), dynamicValidation.dueDate(LocalDate.now()));
	}
	
	@Test
	public void startDateDueDateNull() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		assertEquals(LocalDate.now(), dynamicValidation.startDate(storyModel.getStart_date()));
		assertEquals(LocalDate.now(), dynamicValidation.dueDate(storyModel.getDue_date()));
	}
	
	@Test
	public void storyValidationTrue() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setPath("");
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationNameStatusNullValidation() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("The JSON format provided is invalid, please provide the required fields ('Name','Status').");
		storyDomain.setName("");
		storyDomain.setStatus("");
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationNameNullValidation() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("The JSON format provided is invalid, please provide the required field ('Name').");
		storyDomain.setName("");
		
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationStatusNullValidation() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage("The JSON format provided is invalid, please provide the required field ('Status').");
		storyDomain.setStatus("");
		
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationCharacteres() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		storyDomain.setStatus("Working_");
		storyDomain.setAssignee_id("_");
		
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage(dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()).getMessage());
		
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationCharactere() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		storyDomain.setStatus("Working_");
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setMessage(dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()).getMessage());
		
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationSprintsAssignee() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayConflict();
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.FALSE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.FALSE);
		dynamicValidationArray.setMessage(dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationStatusProgressPoints() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setStatus("Wor");
		storyDomain.setProgress(102);
		storyDomain.setPoints(10);
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void storyValidationProgressPoints() throws Exception {
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setStatus("Wor");
		storyDomain.setProgress(-1);
		storyDomain.setPoints(-1);
		
		when(sprintClient.existsSprintById(storyDomain.getSprint_id())).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.storyValidation(storyDomain, storyDomain.get_id()));
	}
	
	@Test
	public void taskValidationTrue() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		dynamicValidationArray.setPath("");
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		task.setStatus("Working");
		
		when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, null, null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, null, null));
	}
	
	@Test
	public void taskValidationNameStatusNullValidation() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		task.setName("");
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, null, null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, null, null));
	}
	
	@Test
	public void taskValidationCharacters() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		task.setAssignee("_");
		
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, "_", null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, "_", null));
	}
	
	@Test
	public void taskValidationCharacter() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		task.setAssignee("_");
		
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, null, null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, null, null));
	}
	
	@Test
	public void taskValidationAssignee() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayConflict();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		
		when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.FALSE);
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, null, null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, null, null));
	}
	
	@Test
	public void taskValidationStatus() throws Exception {
		storyModel = TestUtils.getStoryTaskModel();
		storyDomain = TestUtils.getStoryDomain();
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		task = TestUtils.getListTaskDomain().get(0);
		task.setStatus("work");
		
		when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.taskValidation(task, null, null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.taskValidation(task, null, null));
	}
	
	@Test
	public void idValidation() throws Exception {
		dynamicValidationArray = TestUtils.getDynamicArray();
	storyDomain.setTasks(TestUtils.getListTaskDomain());
	
	when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.TRUE);
	dynamicValidationArray.setMessage(dynamicValidation.idValidation(null, null).getMessage());
	assertEquals(dynamicValidationArray, dynamicValidation.idValidation(null, null));
	}
	
	@Test
	public void idValidationSome() throws Exception {
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		
		when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.idValidation(":", "_").getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.idValidation(":", "_"));
	}
	
	
	@Test
	public void idValidationOne() throws Exception {
		dynamicValidationArray = TestUtils.getDynamicArrayBadRequest();
		storyDomain.setTasks(TestUtils.getListTaskDomain());
		
		when(usersRepository.existsById(task.getAssignee())).thenReturn(Boolean.TRUE);
		dynamicValidationArray.setMessage(dynamicValidation.idValidation(":", null).getMessage());
		assertEquals(dynamicValidationArray, dynamicValidation.idValidation(":", null));
	}
}
