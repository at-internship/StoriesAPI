package com.stories.healthcheckstests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.stories.constants.StoriesApiTestsConstants;
import com.stories.domain.SprintDomain;
import com.stories.heathchecks.sprintsHealthcheck;
import com.stories.utils.TestUtils;

@RunWith(SpringRunner.class)
public class sprintsHealthcheckTest {
	private TestUtils testUtils;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private sprintsHealthcheck sprintsHealthcheck;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}
	
	@Test
	public void healt() {
		sprintsHealthcheck.health();
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getSprintDomaintList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenReturn(sprintEntity);
		assertEquals(StoriesApiTestsConstants.booleanTrue,
				sprintsHealthcheck.isRunningUsersAPI());
		sprintsHealthcheck.health();
	}
}
