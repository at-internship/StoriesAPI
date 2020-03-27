package com.stories.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
import com.stories.repository.UsersRepository;
import com.stories.utils.TestUtils;

import ma.glasnost.orika.MapperFacade;

@RunWith(SpringRunner.class)
public class ServiceTests {

	@MockBean
	StoriesRepository storiesRepository;

	@MockBean
	private MapperFacade mapperFacade;
	
	@MockBean
	private UsersRepository usersRepository;

	@InjectMocks
	StoriesServiceImpl storiesServiceImpl;

	StoryModel storyModel = new StoryModel();
	StoryDomain storyDomain = new StoryDomain();
	String id = "5e737810acfc726352dc5aba";
	
	List<StoryModel> storiesModel = new ArrayList<StoryModel>();

	private EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Story not found",StoryDomain.class);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getById() throws Exception {
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(storiesRepository.findById(id)).thenReturn(java.util.Optional.of(TestUtils.getDummyStoryModel()));
		when(mapperFacade.map(storyModel, StoryDomain.class)).thenReturn(TestUtils.getDummyStoryDomain());
		when(storiesServiceImpl.getStoryById(id)).thenReturn(TestUtils.getDummyStoryDomain());
		assertEquals(TestUtils.getDummyStoryDomain(), storiesServiceImpl.getStoryById(id));
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdException() throws Exception {
		when(storiesRepository.existsById(id)).thenReturn(Boolean.FALSE);
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
		when(mapperFacade.map(TestUtils.getDummyStoryDomain(), StoryModel.class)).thenReturn(TestUtils.getDummyStoryModel());
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(TestUtils.getDummyStoryModel());
		when(mapperFacade.map(storyDomain, StoryDomain.class)).thenReturn(TestUtils.getDummyStoryDomain());
		assertEquals(TestUtils.getDummyStoryDomain(),
				storiesServiceImpl.updateStory(TestUtils.getDummyStoryDomain(), id));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateStoryExceptionId() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDomain(), StoryModel.class)).thenReturn(storyModel);
		when(storiesRepository.existsById(id)).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.updateStory(TestUtils.getDummyStoryDomain(), id)).thenThrow(entityNotFoundException);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void updateStoryExceptionUser() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDomain(), StoryModel.class)).thenReturn(storyModel);
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(TestUtils.getDummyStoryDomain().getAssignee_id())).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.updateStory(TestUtils.getDummyStoryDomain(), id)).thenThrow(entityNotFoundException);
	}
	@Test(expected = EntityNotFoundException.class)
	public void updateStoryExceptionsTatus() throws Exception {
		when(mapperFacade.map(TestUtils.getDummyStoryDomain(), StoryModel.class)).thenReturn(storyModel);
		when(storiesRepository.existsById(id)).thenReturn(Boolean.TRUE);
		when(usersRepository.existsById(TestUtils.getDummyStoryDomain().getAssignee_id())).thenReturn(Boolean.TRUE);
		Mockito.when(storiesServiceImpl.updateStory(TestUtils.getDummyStoryDomain(), id)).thenThrow(entityNotFoundException);
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
		when(mapperFacade.map(TestUtils.getDummyStoryDomain(), StoryModel.class)).thenReturn(storyModel);
		when(storiesRepository.save(TestUtils.getDummyStoryModel())).thenReturn(storyModel);
		assertEquals(id, storiesServiceImpl.createStory(TestUtils.getDummyStoryDomain()));
	}

	@Test(expected = EntityNotFoundException.class)
	public void createStoryException() throws Exception {
		when(mapperFacade.map(storyDomain, StoryModel.class)).thenReturn(TestUtils.getDummyStoryModel());
		when(usersRepository.existsById(storyDomain.getAssignee_id())).thenReturn(Boolean.FALSE);
		Mockito.when(storiesServiceImpl.createStory(storyDomain)).thenThrow(entityNotFoundException);
	}
}
