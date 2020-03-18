package com.stories.controller;

import com.stories.domain.StoryDomain;
import com.stories.service.StoriesServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/stories")
public class StoriesController {

    @Autowired
    StoriesServiceImpl storyService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/", produces = "application/json")
    public List<StoryDomain> getAllStories() throws Exception {
        return storyService.getAllStories();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = "application/json")
    public StoryDomain getStoryById(@Valid @PathVariable String id) throws Exception {
        return storyService.getStoryById(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public void createStory(@Valid @RequestBody StoryDomain request) throws Exception {
        log.info("Creating story..." + request);
        storyService.createStory(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteStory(@Valid @PathVariable String id) throws Exception {
        log.info("Deleting story with id from the controller: " + id);
        storyService.deleteStory(id);
    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public StoryDomain updateStory(@Valid @RequestBody StoryDomain request, @PathVariable String id) throws Exception {
        return storyService.updateStory(request, id);
    }
}