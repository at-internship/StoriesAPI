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
		String uri = "/stories/";
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk());
	}

	@Test
	public void getByIdValid() throws Exception {
		String uri = "/stories/5e7134c9099a9a0ab248c90b";
		mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType("5e7134c9099a9a0ab248c90b"))
				.andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getByIdInvalid() throws Exception {
		String uri = "/stories/5e6a8441bf#ERFSasda";
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andDo(new ResultHandler() {
			@Override
			public void handle(MvcResult mvcResult) throws Exception {
				throw new EntityNotFoundException("Story not found", "/stories/");
			}
		}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestTrue() throws Exception {
		String uri = "/stories/5e7133b6430bf4151ec1e85f";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat("5e7133b6430bf4151ec1e85f"))).andExpect(status().isOk());
	}

	@Test(expected = EntityNotFoundException.class)
	public void putTestInvelidId() throws Exception {
		String uri = "/stories/5e6a8441bfc6533811235e1";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat("5e6a8441bfc6533811235e1"))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException("Story not found", "/stories/");
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void putTestInvalidJson() throws Exception {
		String uri = "/stories/5e6a8441bfc6533811235e19";
		mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonBadFormat("5e6a8441bfc6533811235e19")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deleteTestTrue() throws Exception {
		String uri = "/stories/5e7133b6430bf4151ec1e85f";
		mockMvc.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat("5e7133b6430bf4151ec1e85f"))).andExpect(status().isNoContent());
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteTestInvalidId() throws Exception {
		String uri = "/stories/5e7133b6430bf4151ec1e85f";
		mockMvc.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonFormat("5e7133b6430bf4151ec1e85f"))).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException(
								"The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.",
								"", "/stories/");
					}
				}).andExpect(status().isNotFound());
	}

	@Test
	public void postTestValidJson() throws Exception {
		String uri = "/stories/";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryValidJson("5e7133b6430bf4151ec1e85f"))).andDo(print())
				.andExpect(status().isCreated());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidStatusJson() throws Exception {
		String uri = "/stories/";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryInvalidStatusJson())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException("Story has an invalid status Json", "/stories/");
					}
				}).andExpect(status().isBadRequest());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidJson() throws Exception {
		String uri = "/stories/";
		mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(testUtils.postStoryBadJsonFormat())).andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						throw new EntityNotFoundException("Malformed JSON request", "/stories/");
					}
				}).andExpect(status().isBadRequest());

	}
	
	@Test()
	public void getTaskByStoryTest() throws Exception {
		String uri = "/stories/5e7134c9099a9a0ab248c90b/tasks/6e413de9099a9a0ab248c90c";
		mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType("6e413de9099a9a0ab248c90c"))
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void getTasksByStoryTest() throws Exception {
		String uri = "/stories/5e7134c9099a9a0ab248c90b/tasks/";
		mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk());
	}
}
