package com.stories.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpStatus;

import com.stories.constants.StoriesApiTestsConstants;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;
import com.stories.utils.TestUtils;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StoriesController.class)
public class ControllerTests {

	@MockBean
	private StoriesServiceImpl storiesServiceImpl;

	@Autowired
	private MockMvc mockMvc;

	private TestUtils testUtils = new TestUtils();

	@Test
	public void getAllValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStories)).andExpect(status().isOk());
	}

	@Test
	public void getByIdValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStory).contentType(StoriesApiTestsConstants.idValid))
				.andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriGetByIdInvalid)).andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult mvcResult) throws Exception {
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
			}
		}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void putTestInvelidId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriGetByIdInvalid).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestInvalidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonBadFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deleteTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andExpect(status().isNoContent());
	}
	
	@Test
    public void deleteTaskTrue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andExpect(status().isNoContent());
    }
	
	@Test(expected = EntityNotFoundException.class)
 	public void deleteTaskInvalidId() throws Exception { 		
	mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask).contentType(MediaType.APPLICATION_JSON_VALUE) 				
		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8") 				
		.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andDo(new ResultHandler() { 					
		@Override 					
		public void handle(MvcResult mvcResult) throws Exception { 						
		throw new EntityNotFoundException(StoriesApiTestsConstants.messageTask, HttpStatus.CONFLICT,StoriesApiTestsConstants.path); 					
		}}).andExpect(status().isConflict()); 
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteTestInvalidId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(
								StoriesApiTestsConstants.messageStatusInvalid,
								StoriesApiTestsConstants.varEmpty, 
								StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void postTestValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid))).andDo(print())
				.andExpect(status().isCreated());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidStatusJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryInvalidStatusJson())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson, StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isBadRequest());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryBadJsonFormat())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(StoriesApiTestsConstants.messageMalformedJSON, StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isBadRequest());
	}
	
	@Test()
	public void getTaskByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTask).contentType("6e413de9099a9a0ab248c90c"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getTasksByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTasks)).andExpect(status().isOk());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTaskInvalid)).andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult mvcResult) throws Exception {
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageIdTask, StoriesApiTestsConstants.pathTask);
				
			}
		}).andExpect(status().isNotFound());
	}
	
	@Test
	public void postTestTaskValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid))).andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void postTaskTestInvalidStatusJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryInvalidStatusJson())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson, StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isBadRequest());
	}
	
	@Test
	public void putTaskByIdTest() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTask).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isOk());
	}
	

	@Test(expected = EntityNotFoundException.class)
	public void putTasktNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTaskInvalid).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
					}
				}).andExpect(status().isNotFound());
	}
	
}
