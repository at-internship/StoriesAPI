package com.stories.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.TestCase;

@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StoriesControllerTests extends TestCase {

	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Override
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void deleteStoryIdValid() throws Exception {
		String uri = "/stories/5e7133b6430bf4151ec1e85f";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 204);
	}

	@Test
	public void deleteStoryIdInValid() throws Exception {
		String uri = "/stories/5e713767ccb57b505d5cab5";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 400);
	}

	@Test
	public void postTestValidJson() throws Exception {
		String uri = "/stories/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(postStoryValidJson()))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 201);
		String conten = mvcResult.getResponse().getContentAsString();
		System.err.println(conten);
	}

	@Test
	public void postTestInvalidStatusJson() throws Exception {
		String uri = "/stories/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(postStoryInvalidStatusJson()))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 400);
		String conten = mvcResult.getResponse().getContentAsString();
		System.err.println(conten);
	}

	@Test
	public void postTestInvalidJson() throws Exception {
		String uri = "/stories/";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").content(postStoryBadJson()))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 400);
		String conten = mvcResult.getResponse().getContentAsString();
		System.err.println(conten);
	}

	private String postStoryValidJson() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	private String postStoryInvalidStatusJson() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Javas\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

	private String postStoryBadJson() {
		return "{\"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":\"2#\",\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}

}
