package com.stories.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.constants.StoriesApiTestsConstants;
import com.stories.domain.SprintDomain;
import com.stories.sprintsclient.SprintsClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SprintsClientTest {
	
	@Autowired
	UnitTestProperties unitTestProperties;

	private TestUtils testUtils;

	@MockBean
	private RestTemplate restTemplate;

	@InjectMocks
	private SprintsClient sprintsClient;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

	@Test
	void existsSprintById() {
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getSprintDomaintList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenReturn(sprintEntity);
		assertEquals(StoriesApiTestsConstants.booleanTrue,
				sprintsClient.existsSprintById(StoriesApiTestsConstants.sprintIdValid));
	}

	@Test
	void noExistsSprintById() {
		ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
				testUtils.getSprintDomaintList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<SprintDomain>>() {
				})).thenReturn(sprintEntity);
		assertEquals(StoriesApiTestsConstants.booleanFalse,
				sprintsClient.existsSprintById(StoriesApiTestsConstants.sprintIdInvalid));
	}

	@Test
	void existsSprintByIdException() {
		Assertions.assertThrows(RestClientException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				ResponseEntity<List<SprintDomain>> sprintEntity = new ResponseEntity<List<SprintDomain>>(
						testUtils.getNullSprintDomaintList(), HttpStatus.NOT_FOUND);
				Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriSprintClient, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<SprintDomain>>() {
						})).thenThrow(new RestClientException(StoriesApiTestsConstants.messageSprints));
				sprintsClient.existsSprintById("5e78f5e792675632e42d1a96");
			}
		});
	}
}