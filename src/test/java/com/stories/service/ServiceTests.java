package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.stories.constants.StoriesApiTestsConstants;
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
@ContextConfiguration
@ExtendWith(SpringExtension.class)
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

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

	MongoTemplate mongoTemplate = Mockito.mock(MongoTemplate.class);

	@Test
	void getById() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getUrlId()))
				.thenReturn(Optional.of(TestUtils.getDummyStoryModel()));
		when(mapperFacade.map(testUtils.getStoryModel(), StoryDomain.class))
				.thenReturn(testUtils.getDummyStoryDomain());
		when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenReturn(testUtils.getDummyStoryDomain());
		assertEquals(testUtils.getDummyStoryDomain(), storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()));
	}

	@Test
	void getByIdException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void getAllStories() throws Exception {
		when(storiesRepository.findAll()).thenReturn(storiesServiceImpl.storiesModel);
		assertEquals(storiesServiceImpl.storiesDomain, storiesServiceImpl.getAllStories());
	}

	@Test
	void getAllStoriesException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.findAll()).thenReturn(TestUtils.listStoriesModelNull());
				when(storiesServiceImpl.getAllStories()).thenThrow(new EntityNotFoundException(
						StoriesApiTestsConstants.messageStories, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void updateStory() throws Exception {
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getModelId()))
				.thenReturn(Optional.of(TestUtils.getStoryModel()));
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.existsById(unitTestProperties.getModelId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(mapperFacade.map(testUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getModelId());
	}

	@Test
	void updateUserException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(unitTestProperties.getModelAssigneeId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
			}
		});
	}

	@Test
	void updateStorykSpecialChar() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.updateStory(testUtils.getStoryDomainSpecialChar(),
						unitTestProperties.getDomainId()))
								.thenThrow(new EntityNotFoundException("The next field have special characters: Status",
										"", "/stories/"));
				storiesServiceImpl.updateStory(testUtils.getStoryDomainSpecialChar(), unitTestProperties.getDomainId());
			}
		});
	}

	@Test
	void updateStorySpecialsChars() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.updateStory(testUtils.getStoryDomainSpecialsChars(),
						unitTestProperties.getDomainId())).thenThrow(
								new EntityNotFoundException("The next field have special characters: Status, assignee",
										"", "/stories/"));
				storiesServiceImpl.updateStory(testUtils.getStoryDomainSpecialsChars(),
						unitTestProperties.getDomainId());
			}
		});
	}

	@Test
	void updateStorySprintException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(unitTestProperties.getModelAssigneeId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
			}
		});
	}

	@Test
	void updateStoryIdException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(unitTestProperties.getModelAssigneeId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.updateStory(testUtils.getStoryDomain(), unitTestProperties.getUrlId());
			}
		});
	}

	@Test
	void updateStoryStatusException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainStatusInvalid().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(testUtils.getStoryDomainStatusInvalid().getSprint_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainStatusInvalid(), StoryModel.class))
						.thenReturn(testUtils.getStoryModel());
				when(storiesServiceImpl.updateStory(testUtils.getStoryDomainStatusInvalid(),
						testUtils.getStoryModel().get_id()))
								.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStatusInvalid,
										StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
				storiesServiceImpl.createStory(testUtils.getStoryDomainStatusInvalid());
			}
		});
	}

	@Test
	void updateException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.updateStory(storiesServiceImpl.storyDomain, testUtils.getStoryModel().get_id()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void deleteStory() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		Mockito.doNothing().when(storiesRepository).deleteById(unitTestProperties.getUrlId());
		storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
	}

	@Test
	void deleteStoryException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.deleteStory(unitTestProperties.getUrlId());
			}
		});
	}

	@Test
	void deleteTask() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getModelId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getModelId()))
				.thenReturn(Optional.of(testUtils.getStoryTaskModel()));
		storiesServiceImpl.deleteTask(testUtils.getStoryTaskModel().get_id(),
				testUtils.getStoryTaskModel().getTasks().get(0).get_id());
	}

	@Test
	void deleteTaskNotFoundException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesRepository.findById(unitTestProperties.getModelId()))
						.thenReturn(Optional.of(testUtils.getStoryTaskModel()));
				storiesServiceImpl.deleteTask(testUtils.getStoryTaskModel().get_id(),
						testUtils.getStoryTaskModel().getTasks().get(0).get_id() + StoriesApiTestsConstants.plusError);
			}
		});
	}

	@Test
	void deleteTaskException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.deleteTask(unitTestProperties.getUrlId(), storiesServiceImpl.storyModel.get_id());
			}
		});
	}

	@Test
	void deleteTaskNullException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(testUtils.getStoryTaskNullModel().get_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesRepository.findById(testUtils.getStoryTaskNullModel().get_id()))
						.thenReturn(Optional.of(testUtils.getStoryTaskNullModel()));
				storiesServiceImpl.deleteTask(unitTestProperties.getModelId(), unitTestProperties.getTasksModelId());
			}
		});
	}

	@Test
	void createTask() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getModelId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(usersRepository.existsById(testUtils.getTasksDomain().getAssignee()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getModelId()))
				.thenReturn(Optional.of(TestUtils.getStoryModel()));
		when(mapperFacade.map(testUtils.getTasksDomain(), TaskModel.class)).thenReturn(TestUtils.getTasksModel());
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(TestUtils.getStoryModel());
		storiesServiceImpl.createTask(testUtils.getTasksDomain(), unitTestProperties.getModelId());
	}

	@Test
	void createTaskSpecialChar() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.createTask(testUtils.getTaskDomainSpecialChar(),
						unitTestProperties.getModelId()))
								.thenThrow(new EntityNotFoundException("The next field have special characters: Status",
										"", "/stories/"));
				storiesServiceImpl.createTask(testUtils.getTaskDomainSpecialChar(), unitTestProperties.getModelId());
			}
		});
	}

	@Test
	void createTaskSpecialsChars() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.createTask(testUtils.getTaskDomainSpecialsChars(),
						unitTestProperties.getModelId())).thenThrow(
								new EntityNotFoundException("The next field have special characters: Status, assignee",
										"", "/stories/"));
				storiesServiceImpl.createTask(testUtils.getTaskDomainSpecialsChars(), unitTestProperties.getModelId());
			}
		});
	}

	@Test
	void createTaskStoryExistException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				when(storiesServiceImpl.createTask(TestUtils.getTasksDomain(), unitTestProperties.getModelId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								HttpStatus.CONFLICT, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createTaskNameException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesServiceImpl.createTask(testUtils.getTasksNameNullDomain(), unitTestProperties.getModelId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.numbreError, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createTaskUserExistException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(usersRepository.existsById(TestUtils.getDummyTasksDomain().getAssignee()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				when(storiesServiceImpl.createTask(testUtils.getTasksDomain(), unitTestProperties.getModelId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								HttpStatus.CONFLICT, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createTaskStatusException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(usersRepository.existsById(testUtils.getTasksDomainStatusValid().getAssignee()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesServiceImpl.createTask(testUtils.getTasksDomainStatusValid(),
						unitTestProperties.getModelId()))
								.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStatusInvalid,
										StoriesApiTestsConstants.numbreError, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createTaskStatusNullException() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getModelId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(usersRepository.existsById(testUtils.getTasksStatusNullDomain().getAssignee()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getModelId()))
				.thenReturn(Optional.of(TestUtils.getStoryModel()));
		when(mapperFacade.map(testUtils.getTasksStatusNullDomain(), TaskModel.class))
				.thenReturn(TestUtils.getTasksModel());
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(TestUtils.getStoryModel());
		storiesServiceImpl.createTask(testUtils.getTasksStatusNullDomain(), unitTestProperties.getModelId());
	}

	@Test
	void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(testUtils.getStoryModel());
		assertEquals(unitTestProperties.getUrlId(), storiesServiceImpl.createStory(TestUtils.getStoryDomain()));
	}

	@Test
	void createStoryStatusException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainStatusInvalid().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(testUtils.getStoryDomainStatusInvalid().getSprint_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainStatusInvalid(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelStatusInvalid());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainStatusInvalid()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStatusInvalid,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
				storiesServiceImpl.createStory(testUtils.getStoryDomainStatusInvalid());
			}
		});
	}

	@Test
	void createStorySprintIdException() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainAssigneInvalid().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainAssigneInvalid(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelAssigneInvalid());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainAssigneInvalid()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageSprintId,
								StoriesApiTestsConstants.path));
				storiesServiceImpl.createStory(testUtils.getStoryDomainAssigneInvalid());
			}
		});
	}

	@Test
	void createStoryNameInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainNameInvalid().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainNameInvalid(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelNameInvalid());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainNameInvalid()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createStoryStatusInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainStatusNull().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainStatusNull(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelStatusNull());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainStatusNull()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createStartDateNull() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainStarDateNull().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainStarDateNull(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelStarDateNull());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainStarDateNull()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createPointsProggresNegative() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainPointsProggresNegative().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(testUtils.getStoryDomainPointsProggresNegative().getSprint_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainPointsProggresNegative(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelPointsProggresNegative());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainPointsProggresNegative()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void createPointsProgressInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(usersRepository.existsById(testUtils.getStoryDomainPointsProgressInvalid().getAssignee_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(sprintsClient.existsSprintById(testUtils.getStoryDomainPointsProgressInvalid().getSprint_id()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(mapperFacade.map(testUtils.getStoryDomainPointsProgressInvalid(), StoryModel.class))
						.thenReturn(testUtils.getStoryModelPointsProgressInvalid());
				when(storiesServiceImpl.createStory(testUtils.getStoryDomainPointsProgressInvalid()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageName,
								StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path));
			}
		});
	}

	@Test
	void getTasksByStory() throws Exception {
		AggregationResults<TasksDomain> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		Aggregation aggregateMock = Mockito.mock(Aggregation.class);

		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class),
				Mockito.eq("stories"), Mockito.eq(TasksDomain.class));
		Mockito.doReturn(testUtils.getTasksDomainList()).when(aggregationResultsMock).getMappedResults();
		when(storiesCustomRepository.getTasksByStory(unitTestProperties.getUrlId())).thenReturn(aggregationResultsMock);
		when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
				.thenReturn(testUtils.getTasksDomainList());

		assertEquals(testUtils.getTasksDomainList(), storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()));
	}

	@Test
	void getTasksByStoryFail() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				when(storiesServiceImpl.getTasksByStory(unitTestProperties.getUrlId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								StoriesApiTestsConstants.path + unitTestProperties.getUrlId()));
			}
		});

	}

	@Test
	void getTaskById() throws Exception {
		AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
		when(storiesRepository.existsById(unitTestProperties.getUrlId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class),
				Mockito.eq("stories"), Mockito.eq(TaskModel.class));
		Mockito.doReturn(testUtils.getTaskModelId()).when(aggregationResultsMock).getUniqueMappedResult();
		when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
				.thenReturn(aggregationResultsMock);
		when(mapperFacade.map(testUtils.getTaskModelId(), TasksDomain.class))
				.thenReturn(testUtils.getDummyTasksDomain());
		when(storiesRepository.findByTasks__id(unitTestProperties.getUrlId())).thenReturn(testUtils.getTaskModelId());
		assertEquals(testUtils.getDummyTasksDomain(),
				storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
	}

	@Test
	void getTaskByIdNoStory() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				when(storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
						.thenThrow(new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
								StoriesApiTestsConstants.path + unitTestProperties.getUrlId()));
			}
		});
	}

	@Test
	void getTaskByIdNoEquals() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class),
						Mockito.eq("stories"), Mockito.eq(TaskModel.class));
				Mockito.doReturn(testUtils.getTaskModelId()).when(aggregationResultsMock).getUniqueMappedResult();
				when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
						.thenReturn(aggregationResultsMock);
				when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
						.thenReturn(testUtils.getDummyTasksDomain());
				when(storiesRepository.findByTasks__id(unitTestProperties.getUrlId()))
						.thenReturn(testUtils.getDummyTaskModel());
				assertEquals(StoriesApiTestsConstants.specificId, testUtils.getDummyTaskModel().get_id());
				assertEquals(testUtils.getDummyTasksDomain(),
						storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
			}
		});
	}

	@Test
	void getTaskByIdTry() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				AggregationResults<TaskModel> aggregationResultsMock = Mockito.mock(AggregationResults.class);
				when(storiesRepository.existsById(unitTestProperties.getUrlId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				Mockito.doReturn(aggregationResultsMock).when(mongoTemplate).aggregate(Mockito.any(Aggregation.class),
						Mockito.eq("stories"), Mockito.eq(TaskModel.class));
				Mockito.doReturn(testUtils.getTaskModelNull()).when(aggregationResultsMock).getUniqueMappedResult();
				when(storiesCustomRepository.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()))
						.thenReturn(aggregationResultsMock);
				when(mapperFacade.map(testUtils.getDummyTaskModel(), TasksDomain.class))
						.thenReturn(testUtils.getDummyTasksDomain());
				assertEquals(testUtils.getDummyTasksDomain(),
						storiesServiceImpl.getTaskById(unitTestProperties.getUrlId(), unitTestProperties.getUrlId()));
			}
		});
	}

	@Test
	void updateTaskbyIdHappyPath() throws Exception {
		when(storiesRepository.existsById(unitTestProperties.getModelId()))
				.thenReturn(StoriesApiTestsConstants.booleanTrue);
		when(storiesRepository.findById(unitTestProperties.getModelId()))
				.thenReturn(java.util.Optional.of(testUtils.getStoryTaskModel()));
		storiesServiceImpl.updateTaskById(testUtils.getUpdateTaskDomain(), unitTestProperties.getModelId(),
				StoriesApiTestsConstants.ValidPutTaskId);
	}

	@Test
	void updateStorySpecialChar() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.updateTaskById(testUtils.getTaskDomainSpecialChar(),
						unitTestProperties.getModelId(), testUtils.getTaskModelId().get_id()))
								.thenThrow(new EntityNotFoundException("The next field have special characters: Status",
										"", "/stories/"));
				storiesServiceImpl.updateTaskById(testUtils.getTaskDomainSpecialChar(), unitTestProperties.getModelId(),
						testUtils.getTaskModelId().get_id());
			}
		});
	}

	@Test
	void updateTaskSpecialsChars() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesServiceImpl.updateTaskById(testUtils.getTaskDomainSpecialsChars(),
						unitTestProperties.getModelId(), testUtils.getTaskModelId().get_id())).thenThrow(
								new EntityNotFoundException("The next field have special characters: Status, assignee",
										"", "/stories/"));
				storiesServiceImpl.updateTaskById(testUtils.getTaskDomainSpecialsChars(),
						unitTestProperties.getModelId(), testUtils.getTaskModelId().get_id());
			}
		});
	}

	@Test
	void updateTaskByIdStoryNotFound() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.updateTaskById(testUtils.getDummyTasksDomain(), unitTestProperties.getModelId(),
						StoriesApiTestsConstants.ValidPutTaskId);
			}
		});
	}

	@Test
	void updateTaskByIdStatusValidation() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				storiesServiceImpl.updateTaskById(testUtils.getUpdateTaskDomainWrongstatus(),
						unitTestProperties.getModelId(), StoriesApiTestsConstants.ValidPutTaskId);
			}
		});
	}

	@Test
	void updateTaskbyIdInvalidAsignee() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(usersRepository.existsById(testUtils.getUpdateTaskDomainAssignee().getAssignee()))
						.thenReturn(StoriesApiTestsConstants.booleanFalse);
				storiesServiceImpl.updateTaskById(testUtils.getUpdateTaskDomainAssignee(),
						unitTestProperties.getModelId(), StoriesApiTestsConstants.ValidPutTaskId);
			}
		});
	}

	@Test
	void updateTaskbyIdNameEmpty() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(usersRepository.existsById(testUtils.getUpdateTaskDomainNameEmpty().getAssignee()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				storiesServiceImpl.updateTaskById(testUtils.getUpdateTaskDomainNameEmpty(),
						unitTestProperties.getModelId(), StoriesApiTestsConstants.ValidPutTaskId);
			}
		});
	}

	@Test
	void updateTaskbyIdInvalidTask() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				when(storiesRepository.existsById(unitTestProperties.getModelId()))
						.thenReturn(StoriesApiTestsConstants.booleanTrue);
				when(storiesRepository.findById(unitTestProperties.getModelId()))
						.thenReturn(java.util.Optional.of(testUtils.getStoryTaskModel()));
				storiesServiceImpl.updateTaskById(testUtils.getUpdateTaskDomain(), unitTestProperties.getModelId(),
						StoriesApiTestsConstants.InvalidId);
			}
		});
	}
}