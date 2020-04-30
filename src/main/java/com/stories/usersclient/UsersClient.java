package com.stories.usersclient;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.domain.UserDomain;

@Component
public class UsersClient {

	private static Logger logger = LogManager.getLogger();

	RestTemplate restTemplate = new RestTemplate();

	public boolean existUserById(String id) throws RestClientException {
		String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/";
		boolean exists = false;
		try {
			ResponseEntity<List<UserDomain>> usersResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserDomain>>() {
					});
			if (usersResponse != null && usersResponse.hasBody()) {
				List<UserDomain> users = usersResponse.getBody();
				logger.debug("Getting all users: " + users);
				for (int i = 0; i < users.size(); i++) {
					if (id.equals(users.get(i).getUserId())) {
						exists = true;
						logger.debug("Getting user if exist: " + users.get(i));
						break;
					}
				}
			}
		} catch (RestClientException e) {
			throw new RestClientException("Users API has no entities");
		}
		return exists;
	}
}
