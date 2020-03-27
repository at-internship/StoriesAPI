package com.stories.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

@RunWith(SpringRunner.class)
public class SprintsClientTest {

	@Autowired
	UnitTestProperties unitTestProperties;

	private TestUtils testUtils;

	@InjectMocks
	SprintsClient sprintsClient;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

	@Ignore
	@Test
	public void existsSprintById() throws Exception {
		
	}
}