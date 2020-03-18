package com.stories.controller;

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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(StoriesController.class)
public class PostMethodTests {

	@MockBean
	private StoriesServiceImpl storiesServiceImpl;

	@Autowired
	private MockMvc mvcResult;

	@Test
	public void postTestValidJson() throws Exception {
		String uri = "/stories/";
		mvcResult
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
						.content(postStoryValidJson("5e7133b6430bf4151ec1e85f")))
				.andDo(print()).andExpect(status().isCreated());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidStatusJson() throws Exception {
		String uri = "/stories/";
		mvcResult.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(postStoryInvalidStatusJson())).andDo(new ResultHandler() {
				@Override
				public void handle(MvcResult mvcResult) throws Exception {
					throw new EntityNotFoundException("Story has an invalid status Json", StoryDomain.class);
					}
				}).andExpect(status().isBadRequest());
	}

	@Test(expected = EntityNotFoundException.class)
	public void postTestInvalidJson() throws Exception {
		String uri = "/stories/";
        mvcResult.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(postStoryBadJsonFormat())).andExpect(status().isBadRequest());
	}

	private String postStoryValidJson(String id) {
		return "{\"id\":\"" + id
				+ "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	private String postStoryInvalidStatusJson() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	private String postStoryBadJsonFormat() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\"%,\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";	}
}
