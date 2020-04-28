package com.stories.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.stories.constants.StoriesApiTestsConstants;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;
import com.stories.utils.TestUtils;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StoriesController.class)
public class ControllerTests {

	@MockBean
	private StoriesServiceImpl storiesServiceImpl;

	@Autowired
	private MockMvc mockMvc;

	private TestUtils testUtils = new TestUtils();

	@Test
	void getAllValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStories)).andExpect(status().isOk());
	}

	@Test
	void getByIdValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStory)
				.contentType(StoriesApiTestsConstants.idValid)).andExpect(status().isOk());
	}

	@Test
	void getByIdInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriGetByIdInvalid))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
										StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isNotFound());
			}
		});
	}

	@Test
	void putTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isOk());
	}

	@Test
	void putTestInvelidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriGetByIdInvalid)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
										StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isNotFound());
			}
		});
	}

	@Test
	void putTestInvalidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(testUtils.setStoryInJsonBadFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deleteTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteTaskTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteTaskInvalidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageTask,
										HttpStatus.CONFLICT, StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isConflict());
			}
		});
	}

	@Test
	void deleteTestInvalidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStatusInvalid,
										StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isNotFound());
			}
		});
	}

	@Test
	void postTestValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid)))
				.andDo(print()).andExpect(status().isCreated());
	}

	@Test
	void postTestInvalidStatusJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryInvalidStatusJson()))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson,
										StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isBadRequest());
			}
		});
	}

	@Test
	void postTestInvalidJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryBadJsonFormat()))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageMalformedJSON,
										StoriesApiTestsConstants.numbreError, StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isBadRequest());
			}
		});
	}

	@Test
	void getTaskByStoryTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTask).contentType("6e413de9099a9a0ab248c90c"))
				.andExpect(status().isOk());
	}

	@Test
	void getTasksByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTasks)).andExpect(status().isOk());
	}

	@Test
	void getTaskByIdInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTaskInvalid))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageIdTask,
										StoriesApiTestsConstants.pathTask);
							}
						}).andExpect(status().isNotFound());
			}
		});
	}

	@Test
	void postTestTaskValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid)))
				.andDo(print()).andExpect(status().isCreated());
	}

	@Test
	void postTaskTestInvalidStatusJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryInvalidStatusJson()))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson,
										StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isBadRequest());
			}
		});
	}

	@Test
	void putTaskByIdTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTask)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isOk());
	}

	@Test
	void putTasktNotFound() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTaskInvalid)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
						.andDo(new ResultHandler() {
							@Override
							public void handle(MvcResult mvcResult) throws Exception {
								throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory,
										StoriesApiTestsConstants.path);
							}
						}).andExpect(status().isNotFound());
			}
		});
	}

}
