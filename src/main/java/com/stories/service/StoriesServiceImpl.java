package com.stories.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;
import com.stories.sprintsclient.SprintsClient;

import io.micrometer.core.instrument.util.StringUtils;
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

	String[] statusArray = { "Refining", "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
	StoryModel storyModel = new StoryModel();
	List<StoryModel> storiesModel = new ArrayList<StoryModel>();
	StoryDomain storyDomain = new StoryDomain();
	List<StoryDomain> storiesDomain = new ArrayList<>();

	@Autowired
	SprintsClient sprintClient;

	@Override
	public String createStory(StoryDomain storyDomain) throws Exception {
		if (nameStatusNullValidation(storyDomain.getName(), storyDomain.getStatus())) {
			if (userNullValidation(storyDomain.getAssignee_id())) {
				if (sprintNullValidation(storyDomain.getSprint_id())) {
					storyDomain.setStart_date(startDateValidation(storyDomain.getStart_date()));
					storyModel = mapperFacade.map(storyDomain, StoryModel.class);
					if (statusValidation(statusArray, storyModel.getStatus())) {
						logger.debug("Creating story with the json : {}", storyModel);
						String id = nameValidation(storyModel).get_id();
						return id;
					} else {
						throw new EntityNotFoundException(
								"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
								400, "", "/stories/");
					}
				} else {
					throw new EntityNotFoundException("The sprint_id does not exists", "/sprints/");
				}
			} else {
				throw new EntityNotFoundException("The user provided does not exist", "/stories/");
			}
		}
		throw new EntityNotFoundException(
				"The JSON format provided is invalid, please provide the required fields ('Name','Status').", 400, "",
				"/stories/");
	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found", HttpStatus.CONFLICT,
					"/stories/");
		} else
			logger.debug("Deleting story with the id: " + id);
		storiesRepository.deleteById(id);
	}

	@Override
	public StoryDomain updateStory(StoryDomain storyDomain, String id) throws Exception {
		if (nameStatusNullValidation(storyDomain.getName(), storyDomain.getStatus())) {
			if (userNullValidation(storyDomain.getAssignee_id())) {
				if (sprintNullValidation(storyDomain.getSprint_id())) {
					if (storiesRepository.existsById(id)) {
						storyDomain.setStart_date(startDateValidation(storyDomain.getStart_date()));
						storyModel = mapperFacade.map(storyDomain, StoryModel.class);
						if (statusValidation(statusArray, storyModel.getStatus())) {
							storyModel.set_id(id);
							nameValidation(storyModel);
							storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
							logger.debug("Updating story with the id: " + id + " - JSON : {}", storyDomain);
							return storyDomain;
						} else {
							throw new EntityNotFoundException(
									"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
									400, "", "/stories/");
						}
					} else {
						throw new EntityNotFoundException("Story not found", "/stories/");
					}
				} else {
					throw new EntityNotFoundException("The sprint_id does not exists", "/sprints/");
				}
			} else {
				throw new EntityNotFoundException("The user provided does not exist", "/stories/");
			}
		} else {
			throw new EntityNotFoundException(
					"The JSON format provided is invalid, please provide the required fields ('Name','Status').", 400,
					"", "/stories/");
		}
	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", "/stories/");
		storyModel = storiesRepository.findById(id).get();
		storyDomain = mapperFacade.map(storyModel, StoryDomain.class);
		logger.debug("Getting story with the id: " + id + " - JSON : {}", storyDomain);
		return storyDomain;
	}

	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> allStoriesModel = new ArrayList<>();
		List<StoryDomain> allStoriesDomain = new ArrayList<>();
		allStoriesModel = storiesRepository.findAll();
		if (allStoriesModel == null)
			throw new EntityNotFoundException("Stories not found", "/stories/");
		for (int i = 0; i < allStoriesModel.size(); i++) {
			allStoriesDomain.add(mapperFacade.map(allStoriesModel.get(i), StoryDomain.class));
		}
		logger.debug("Getting all stories - JSON : {}", allStoriesDomain);
		return allStoriesDomain;
	}

	private boolean statusValidation(String[] statusArray, String storyStatus) {
		return Arrays.asList(statusArray).contains(storyStatus);
	}

	private StoryModel nameValidation(StoryModel storyModel) throws EntityNotFoundException {
		try {
			storiesRepository.save(storyModel);
			return storyModel;
		} catch (Exception e) {
			throw new EntityNotFoundException("There is a story with this name already.", 400, "", "/stories/");
		}
	}

	private boolean userNullValidation(String assigneeId) throws EntityNotFoundException {
		if (StringUtils.isEmpty(assigneeId)) {
			return true;
		} else {
			if (!usersRepository.existsById(assigneeId))
				throw new EntityNotFoundException("The user provided does not exist", "/stories/");
			return true;
		}
	}

	private boolean sprintNullValidation(String sprintId) throws EntityNotFoundException {
		if (StringUtils.isEmpty(sprintId)) {
			return true;
		} else {
			if (!sprintClient.existsSprintById(sprintId))
				throw new EntityNotFoundException("The sprint_id does not exists", "/sprints/");
			return true;
		}
	}

	private boolean nameStatusNullValidation(String name, String status) throws EntityNotFoundException {
		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(status)) {
			throw new EntityNotFoundException(
					"The JSON format provided is invalid, please provide the required fields ('Name','Status').", 400,
					"", "/stories/");
		} else if (StringUtils.isEmpty(name)) {
			throw new EntityNotFoundException(
					"The JSON format provided is invalid, please provide the required field ('Name').", 400, "",
					"/stories/");
		} else if (StringUtils.isEmpty(status)) {
			throw new EntityNotFoundException(
					"The JSON format provided is invalid, please provide the required field ('Status').", 400, "",
					"/stories/");
		}
		return true;
	}

	private LocalDate startDateValidation(LocalDate start_date) {
		if ((!(start_date == null || (StringUtils.isEmpty(start_date.toString()))))) {
			return start_date;
		} else {
			return LocalDate.now();
		}
	}
}