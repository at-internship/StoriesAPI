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

import com.stories.constants.StoriesApiConstants;
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
	private StoriesApiConstants storiesApiConstants = new StoriesApiConstants();

	@Test
	public void getAllValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriStories())).andExpect(status().isOk());
	}

	@Test
	public void getByIdValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriStory()).contentType(storiesApiConstants.getIdValid()))
				.andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriGetByIdInvalid())).andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult mvcResult) throws Exception {
				throw new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath());
			}
		}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(storiesApiConstants.getUriStory()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void putTestInvelidId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(storiesApiConstants.getUriGetByIdInvalid()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(storiesApiConstants.getMessageStory(), storiesApiConstants.getPath());
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestInvalidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(storiesApiConstants.getUriStory()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonBadFormat(storiesApiConstants.getIdValid())))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deleteTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(storiesApiConstants.getUriStory()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andExpect(status().isNoContent());
	}
	
	@Test
    public void deleteTaskTrue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(storiesApiConstants.getUriTask()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andExpect(status().isNoContent());
    }
	
	@Test(expected = EntityNotFoundException.class)
 	public void deleteTaskInvalidId() throws Exception { 		
	mockMvc.perform(MockMvcRequestBuilders.delete(storiesApiConstants.getUriTask()).contentType(MediaType.APPLICATION_JSON_VALUE) 				
		.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8") 				
		.content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andDo(new ResultHandler() { 					
		@Override 					
		public void handle(MvcResult mvcResult) throws Exception { 						
		throw new EntityNotFoundException(storiesApiConstants.getMessageTask(), HttpStatus.CONFLICT,storiesApiConstants.getPath()); 					
		}}).andExpect(status().isConflict()); 
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteTestInvalidId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(storiesApiConstants.getUriStory()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat(storiesApiConstants.getIdValid()))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(
								storiesApiConstants.getMessageStatusInvalid(),
								storiesApiConstants.getVarEmpty(), 
								storiesApiConstants.getPath());
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void postTestValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(storiesApiConstants.getUriStories()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryValidJson(storiesApiConstants.getIdValid()))).andDo(print())
				.andExpect(status().isCreated());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidStatusJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(storiesApiConstants.getUriStories()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryInvalidStatusJson())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(storiesApiConstants.getMessageStoryJson(), storiesApiConstants.getPath());
					}
				}).andExpect(status().isBadRequest());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(storiesApiConstants.getUriStories()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryBadJsonFormat())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(storiesApiConstants.getMessageMalformedJSON(), storiesApiConstants.getPath());
					}
				}).andExpect(status().isBadRequest());
	}
	
	@Test()
	public void getTaskByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriTask()).contentType("6e413de9099a9a0ab248c90c"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getTasksByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriTasks())).andExpect(status().isOk());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void getTaskByIdInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(storiesApiConstants.getUriTaskInvalid())).andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult mvcResult) throws Exception {
				throw new EntityNotFoundException(storiesApiConstants.getMessageIdTask(), storiesApiConstants.getPathTask());
				
			}
		}).andExpect(status().isNotFound());
	}
	
	@Test
	public void postTestTaskValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(storiesApiConstants.getUriTasks()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryValidJson(storiesApiConstants.getIdValid()))).andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void postTaskTestInvalidStatusJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(storiesApiConstants.getUriTasks()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryInvalidStatusJson())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(storiesApiConstants.getMessageStoryJson(), storiesApiConstants.getPath());
					}
				}).andExpect(status().isBadRequest());
	}
}
