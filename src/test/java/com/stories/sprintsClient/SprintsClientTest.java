package com.stories.sprintsClient;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.constants.StoriesApiTestsConstants;
import com.stories.domain.SprintDomain;
import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

@RunWith(SpringRunner.class)
public class SprintsClientTest {

	UnitTestProperties unitTestProperties;

	private TestUtils testUtils;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private SprintsClient sprintsClient;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

	@Test
	public void existsSprintById() throws Exception {
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getSprintDomaintList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenReturn(sprintEntity);
		assertEquals(StoriesApiTestsConstants.booleanTrue,
				sprintsClient.existsSprintById(StoriesApiTestsConstants.sprintIdValid));
	}

	@Test
	public void noExistsSprintById() throws Exception {
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getSprintDomaintList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenReturn(sprintEntity);
		assertEquals(StoriesApiTestsConstants.booleanFalse,
				sprintsClient.existsSprintById(StoriesApiTestsConstants.sprintIdInvalid));
	}

	@Test(expected = RestClientException.class)
	public void existsSprintByIdException() throws Exception {
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getNullSprintDomaintList(), HttpStatus.NOT_FOUND);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenThrow(new RestClientException(StoriesApiTestsConstants.messageSprints));
		sprintsClient.existsSprintById("5e78f5e792675632e42d1a96");
	}
}