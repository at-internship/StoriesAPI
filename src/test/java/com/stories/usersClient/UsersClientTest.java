package com.stories.usersClient;

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
import com.stories.domain.UserDomain;
import com.stories.usersclient.UsersClient;
import com.stories.utils.TestUtils;
import com.stories.utils.UnitTestProperties;

@RunWith(SpringRunner.class)
public class UsersClientTest {

	UnitTestProperties unitTestProperties;

	private TestUtils testUtils;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private UsersClient usersClient;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testUtils = new TestUtils();
	}

	@Test
	public void existsUserById() throws Exception {
		ResponseEntity<List<UserDomain>> userEntity = new ResponseEntity<List<UserDomain>>(
				testUtils.getUserDomainList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriUserClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDomain>>() {
				})).thenReturn(userEntity);
		assertEquals(StoriesApiTestsConstants.booleanTrue,
				usersClient.existUserById(StoriesApiTestsConstants.userIdValid));
	}

	@Test
	public void noExistsUsertById() throws Exception {
		ResponseEntity<List<UserDomain>> userEntity = new ResponseEntity<List<UserDomain>>(
				testUtils.getUserDomainList(), HttpStatus.OK);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriUserClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDomain>>() {
				})).thenReturn(userEntity);
		assertEquals(StoriesApiTestsConstants.booleanFalse,
				usersClient.existUserById(StoriesApiTestsConstants.userIdInvalid));
	}

	@Test(expected = RestClientException.class)
	public void existsUserByIdException() throws Exception {
		ResponseEntity<List<UserDomain>> userEntity = new ResponseEntity<List<UserDomain>>(
				testUtils.getNullUserDomaintList(), HttpStatus.NOT_FOUND);
		Mockito.when(restTemplate.exchange(StoriesApiTestsConstants.uriUserClient, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<UserDomain>>() {
				})).thenThrow(new RestClientException(StoriesApiTestsConstants.messageUsers));
		usersClient.existUserById("5ea7125ce6cd3109e8bc71cf");
	}
}