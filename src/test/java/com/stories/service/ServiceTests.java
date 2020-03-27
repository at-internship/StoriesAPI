package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
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
				.thenReturn(TestUtils.getDummyStoryDoamin());
		when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenReturn(TestUtils.getDummyStoryDoamin());
		assertEquals(TestUtils.getDummyStoryDoamin(), storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdException() throws Exception {
		when(!storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.getStoryById(unitTestProperties.getUrlId()))
				.thenThrow(new EntityNotFoundException("Story not found", StoryDomain.class));
	}

	@Ignore
	@Test
	public void getAllStories() throws Exception {
		when(storiesRepository.findAll()).thenReturn(testUtils.getStoryModelList());
		assertEquals(storiesServiceImpl.getAllStories(), testUtils.getStoryModelList());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getAllStoriesException() throws Exception {
		when(storiesRepository.findAll()).thenReturn(TestUtils.listStoriesModelNull());
		Mockito.when(storiesServiceImpl.getAllStories())
				.thenThrow(new EntityNotFoundException("Story not found", StoryDomain.class));
	}

	@Ignore
	@Test
	public void updateStory() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDoamin(), StoryModel.class))
				.thenReturn(TestUtils.getDummyStoryModel());
		when(storiesRepository.existsById(unitTestProperties.getUrlId())).thenReturn(Boolean.TRUE);
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(TestUtils.getDummyStoryModel());
		when(mapperFacade.map(testUtils.getStoryDomain(), StoryDomain.class))
				.thenReturn(TestUtils.getDummyStoryDoamin());
		assertEquals(TestUtils.getDummyStoryDoamin(),
				storiesServiceImpl.updateStory(TestUtils.getDummyStoryDoamin(), unitTestProperties.getUrlId()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryException() throws Exception {
		when(sprintsClient.existsSprintById(unitTestProperties.getDomainSprintId() + "S")).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.updateStory(TestUtils.getStoryDomain(), unitTestProperties.getModelId()))
				.thenThrow(new EntityNotFoundException("The sprint is not exists", SprintsClient.class));
		storiesServiceImpl.updateStory(TestUtils.getStoryDomain(), unitTestProperties.getUrlId());
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

	@Ignore
	@Test
	public void createStory() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDoamin(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(testUtils.getStoryModel());
		assertEquals(unitTestProperties.getUrlId(), storiesServiceImpl.createStory(TestUtils.getDummyStoryDoamin()));
	}

	@Ignore
	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(testUtils.getStoryModel());
		Mockito.doReturn(Boolean.FALSE).when(sprintsClient).existsSprintById(unitTestProperties.getDomainSprintId());
		throw new EntityNotFoundException("The sprint_id does not exists", SprintsClient.class);
	}
}