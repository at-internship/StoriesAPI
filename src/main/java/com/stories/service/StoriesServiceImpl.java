package com.stories.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class StoriesServiceImpl implements StoriesService {

	@Autowired
	StoriesRepository storiesRepository;
	
	@Autowired
	UsersRepository usersRepository;

	private static Logger logger = LogManager.getLogger();

	@Autowired
	private MapperFacade mapperFacade;
	
	@Override
	public String createStory(StoryDomain storyDomain) throws Exception {
		StoryModel storyModel = new StoryModel();
		storyModel = mapperFacade.map(storyDomain, StoryModel.class);
		String storystatus = storyModel.getStatus();
		String[] statusArray = { "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		boolean test = Arrays.asList(statusArray).contains(storystatus);
		if(!usersRepository.existsById(storyDomain.getAssignee_id()))
				throw new EntityNotFoundException("The user does not exist");
		if (test) {
			try {
				logger.debug("Creating story with the json : {}", storyModel);
				return storiesRepository.save(storyModel).get_id().toString();
			} catch (Exception e) {
				throw new EntityNotFoundException("There is a story with this name already", e.getMessage(),
						StoryDomain.class);
			}
		} else {
			throw new EntityNotFoundException("The Status field should be one of the following options: 'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
                    "", StoryDomain.class);
		}
	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found", StoryModel.class);
		} else
			logger.debug("Deleting story with the id: " + id);
		storiesRepository.deleteById(id);
	}

	public StoryDomain updateStory(StoryDomain storyDomain, String id) throws Exception {
		StoryModel storyModel = mapperFacade.map(storyDomain, StoryModel.class);
		String storystatus = storyModel.getStatus();
		String[] statusArray = { "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		boolean test = Arrays.asList(statusArray).contains(storystatus);
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		if (!usersRepository.existsById(storyDomain.getAssignee_id()))
			throw new EntityNotFoundException("The user does not exist", StoryDomain.class);
		if (test) {
			try {
				storyModel.set_id(id);
				storiesRepository.save(storyModel);
				storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
				logger.debug("Updating story with the id: " + id + " - JSON : {}", storyDomain);
				return storyDomain;
			} catch (Exception e) {
				throw new EntityNotFoundException("There is a story with this name already", e.getMessage(),
						StoryDomain.class);
			}
		}else {
			throw new EntityNotFoundException("The Status field should be one of the following options: 'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
                    "", StoryDomain.class);
		}
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		StoryDomain storyDomain = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel storyModel = storiesRepository.findById(id).get();
		storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
		logger.debug("Getting story with the id: " + id + " - JSON : {}", storyDomain);
		return storyDomain;
	}

	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> storiesModel = new ArrayList<StoryModel>();
		storiesModel = storiesRepository.findAll();
		List<StoryDomain> storiesDomain = new ArrayList<>();
		if (storiesModel == null)
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		for (int i = 0; i < storiesModel.size(); i++) {
			storiesDomain.add(mapperFacade.map(storiesModel.get(i), StoryDomain.class));
		}
		logger.debug("Getting all stories - JSON : {}", storiesDomain);
		return storiesDomain;
	}
}