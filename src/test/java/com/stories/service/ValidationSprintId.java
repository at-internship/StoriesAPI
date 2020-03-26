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
import com.stories.utils.TestUtils;

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

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void putSprintValidationTrue() throws Exception {
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(TestUtils.getStoryModel(TestUtils.storyId));
		when(sprintsClient.existsSprintById(TestUtils.sprintId)).thenReturn(Boolean.TRUE);
		when(storiesRepository.existsById(TestUtils.storyId)).thenReturn(Boolean.TRUE);
		when(storiesServiceImpl.updateStory(TestUtils.getStoryDomain(), TestUtils.storyId)).thenReturn(TestUtils.getStoryDomain());
		assertEquals(TestUtils.getStoryDomain(), storiesServiceImpl.updateStory(TestUtils.getStoryDomain(), TestUtils.storyId));
	}

	@Test(expected = EntityNotFoundException.class)
	public void putSprintIdExeption() throws Exception {
		when(sprintsClient.existsSprintById(TestUtils.sprintId)).thenReturn(Boolean.FALSE);
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			throw new EntityNotFoundException("The sprint is not exists", SprintsClient.class);
		});
		assertEquals(exception, storiesServiceImpl.updateStory(TestUtils.getStoryDomain(), TestUtils.storyId));
	}

	@Test
	public void postSprintValidationTrue() throws Exception {
		when(sprintsClient.existsSprintById(TestUtils.sprintId)).thenReturn(Boolean.TRUE);
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(TestUtils.getStoryModel(TestUtils.storyId));
		when(storiesRepository.save(TestUtils.getStoryModel(TestUtils.storyId))).thenReturn(TestUtils.getStoryModel(TestUtils.storyId));
		assertEquals(TestUtils.storyId, storiesServiceImpl.createStory(TestUtils.getStoryDomain()));
	}

	@Ignore
	@Test(expected = EntityNotFoundException.class)
	public void postSprintValidationException() throws Exception {
		when(sprintsClient.existsSprintById(TestUtils.sprintId)).thenReturn(Boolean.FALSE);
		when(mapperFacade.map(TestUtils.getStoryDomain(), StoryModel.class)).thenReturn(TestUtils.getStoryModel(TestUtils.storyId));
		throw new EntityNotFoundException("The sprint_id does not exists", SprintsClient.class);
	}
}