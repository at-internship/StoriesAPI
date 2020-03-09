package com.stories.Controller.Config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.sym.Name;
import com.stories.model.Story_task;
import com.stories.model.stories;
//import com.stories.repository.StoriesRepository;
import com.stories.repository.Story_task_repository;
import com.stories.repository.storiesRepository;


@RestController
@RequestMapping(value = "/stories-qa.us-east-2.elasticbeanstalk.com/stories" , produces = "application/josn")
public class Controller_Config{
	

	@Autowired
	private Story_task_repository Stories_repo;
	
//	@Autowired
//	private storiesRepository stories_repo;
//
//	
//	@PutMapping(value = "/{id}")
//	public stories GetStories (@PathVariable String id){
////		stories pA = new stories("1","A");
//		return stories_repo.save(new stories("2","a"));
//	}
//
//	@GetMapping(value = "/{id}")
//	public Optional<stories> GestStories (@PathVariable String id){
//		return stories_repo.findById(id);
//	}
//	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public List<Story_task> GetStories(@PathVariable int id) {

		
		return Stories_repo.findAll();
//		if(Stories_repo.findById(id) == null) {
//			return  Stories_repo.findById(id);
//		} 
//		else {
//			return  Stories_repo.findById(id);
//		}
	}

//	@PutMapping("/a")
//	@ResponseStatus(HttpStatus.RESET_CONTENT)
//	public String add() {
//		
//		return "c";
//	}
	
	
//	@Autowired
//	private Story_task Stories;
	
//	@RequestMapping(value="/{id}")
//	public int getStroies(@PathVariable int id) {
//		return id;
//	}
	
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//	public ResponseEntity<Story_task> Stories(@PathVariable int id, @RequestBody Story_task Stories){
////		if(Stories.FindById(id) == null) {
////			
////		}
//		
////		if(service.findBy(id) == null) {
////			return ResponseEntity<String>("Story_task not found", HttpStatus.NOT_FOUND);
////		}
////		else {
////			service.update(user_story);
////			return new ResponseEntity<String>("Story_task is update", HttpStatus.OK);
////		}
////		if(id == 0) {
////			return ResponseEntity<Stories>("Story_task not found", HttpStatus.NOT_FOUND);
////		}
////		else {
////			
////			return new ResponseEntity<Stories>("Story_task is update", HttpStatus.OK);
////		}
//		return Stories;
//	}
}






