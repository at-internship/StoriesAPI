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
	public void putTestTrue() throws Exception {
		String uri = "/stories/5e713308b7872622cb3d10ea";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(getStoryInJson("5e713308b7872622cb3d10ea"))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 202);
	}

	@Test
	public void putTestInvelidId() throws Exception {
		String uri = "/stories/5e6a8441bfc6533811235e1";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(getStoryInJson("5e6a8441bfc6533811235e1"))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 404);
	}
	
	@Test
	public void putTestInvalidJson() throws Exception {
		String uri = "/stories/5e6a8441bfc6533811235e19";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(getStoryInJsonBad("5e6a8441bfc6533811235e19"))).andReturn();
				
		int status = mvcResult.getResponse().getStatus();
		assertEquals(status, 400);
	}

	private String getStoryInJson(String id) {
		return "{\"id\":\"" + id + "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}
	
	private String getStoryInJsonBad(String id) {
		return "{\"id\":\"" + id + "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":\"2#\",\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
	}
}
