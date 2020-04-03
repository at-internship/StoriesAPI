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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
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
	public void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		when(usersRepository.existsById(unitTestProperties.getModelAssigneeId())).thenReturn(true);
		when(sprintsClient.existsSprintById(unitTestProperties.getSprintClientId())).thenReturn(true);
		when(storiesRepository.save(TestUtils.getStoryModel())).thenReturn(testUtils.getStoryModel());
		assertEquals(unitTestProperties.getUrlId(), storiesServiceImpl.createStory(TestUtils.getStoryDomain()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		when(storiesServiceImpl.createStory(storiesServiceImpl.storyDomain))
				.thenThrow(new EntityNotFoundException("Story not found", "/stories/"));
	}
}