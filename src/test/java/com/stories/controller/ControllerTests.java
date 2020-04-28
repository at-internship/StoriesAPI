package com.stories.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
	@DisplayName("GET all US")
	void getAllValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStories)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET US by Id")
	void getByIdValid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriStory)
				.contentType(StoriesApiTestsConstants.idValid)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET US by invalid Id exception")
	void getByIdInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriGetByIdInvalid);
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("PUT US by id")
	void putTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("PUT US by invalid US Id Exception")
	void putTestInvelidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriGetByIdInvalid)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("PUT US by invalid JSON Exception")
	void putTestInvalidJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriStory)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonBadFormat(StoriesApiTestsConstants.idValid)));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson,
						StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("DELETE US by Id")
	void deleteTestTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("DELETE TASK by Id")
	void deleteTaskTrue() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("DELETE task by Invalid Id Exception")
	void deleteTaskInvalidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriTask)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageTask, HttpStatus.CONFLICT,
						StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("DELETE task by invalid US Id Exception")
	void deleteTestInvalidId() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.delete(StoriesApiTestsConstants.uriStory)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStatusInvalid,
						StoriesApiTestsConstants.varEmpty, StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("POST US valid JSON")
	void postTestValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid)))
				.andDo(print()).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("POST US by invalid Status Exception")
	void postTestInvalidStatusJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryInvalidStatusJson()));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson,
						StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("POST US by invalid JSON Exception")
	void postTestInvalidJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriStories)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryBadJsonFormat()));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageMalformedJSON,
						StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("GET task by Id")
	void getTaskByStoryTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTask).contentType("6e413de9099a9a0ab248c90c"))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET All tasks of an US")
	void getTasksByStoryTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTasks)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET task by invalid Id Exception")
	void getTaskByIdInvalid() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.get(StoriesApiTestsConstants.uriTaskInvalid));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageIdTask,
						StoriesApiTestsConstants.pathTask);
			}
		});
	}

	@Test
	@DisplayName("POST task")
	void postTestTaskValidJson() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.postStoryValidJson(StoriesApiTestsConstants.idValid)))
				.andDo(print()).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("POST task by invalid JSON Exception")
	void postTaskTestInvalidStatusJson() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.post(StoriesApiTestsConstants.uriTasks)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryInvalidStatusJson()));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStoryJson,
						StoriesApiTestsConstants.path);
			}
		});
	}

	@Test
	@DisplayName("PUT task")
	void putTaskByIdTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTask)
				.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(testUtils.setStoryInJsonFormat(StoriesApiTestsConstants.idValid)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("PUT task by invalid Id Exception")
	void putTasktNotFound() {
		Assertions.assertThrows(EntityNotFoundException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				mockMvc.perform(MockMvcRequestBuilders.put(StoriesApiTestsConstants.uriTaskInvalid)
						.contentType(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8").content(testUtils.postStoryInvalidStatusJson()));
				throw new EntityNotFoundException(StoriesApiTestsConstants.messageStory, StoriesApiTestsConstants.path);
			}
		});
	}

}
