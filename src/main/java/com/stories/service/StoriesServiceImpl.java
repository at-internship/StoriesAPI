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

import ma.glasnost.orika.MapperFacade;

@Service
public class StoriesServiceImpl implements StoriesService {

	@Autowired
	StoriesRepository storiesRepository;

	private static Logger logger = LogManager.getLogger();

	@Autowired
	private MapperFacade mapperFacade;

	@Override
	public String createStory(StoryDomain request) throws Exception {
		StoryModel storyModel = new StoryModel();
		storyModel = mapperFacade.map(request, StoryModel.class);

		String storystatus = storyModel.getStatus();
		String[] statusArray = { "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		boolean test = Arrays.asList(statusArray).contains(storystatus);

		if (test) {
			try {
				logger.debug("Creating story.... - Body : {}", storyModel);
				return storiesRepository.save(storyModel).get_id().toString();
			} catch (Exception e) {
				throw new EntityNotFoundException("There is a story with this name already", e.getMessage(),
						StoryDomain.class);
			}
		} else {
			throw new EntityNotFoundException("Status json state is invalid",
					"The status should be: Ready to Work, Working, Testing, Ready to Accept or Accepted.",
					StoryDomain.class);
		}

	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found", StoryModel.class);
		} else
			logger.debug("Deleting story.... " + id);
		storiesRepository.deleteById(id);
	}

	public StoryDomain updateStory(StoryDomain request, String id) throws Exception {
		StoryDomain storyDomain = mapperFacade.map(request, StoryDomain.class);
		StoryModel story = mapperFacade.map(storyDomain, StoryModel.class);
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		story.set_id(id);
		storiesRepository.save(story);
		storyDomain = mapperFacade.map(story, StoryDomain.class);
		logger.debug("Updating story with the id: " + id + " - Body : {}", storyDomain);
		return storyDomain;
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		StoryDomain story = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel storyModel = storiesRepository.findById(id).get();
		story = mapperFacade.map(storyModel, StoryDomain.class);
		logger.debug("Getting story with the id: " + id + " - Body : {}", story);
		return story;
	}

	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> story = new ArrayList<StoryModel>();
		story = storiesRepository.findAll();
		List<StoryDomain> stories = new ArrayList<>();
		if (story == null)
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		for (int i = 0; i < story.size(); i++) {
			stories.add(mapperFacade.map(story.get(i), StoryDomain.class));
		}
		logger.debug("Getting all stories - Body : {}", stories);
		return stories;
	}
}
