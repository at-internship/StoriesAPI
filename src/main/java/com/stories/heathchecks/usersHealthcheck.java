package com.stories.heathchecks;

import java.util.List;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.domain.UserDomain;

@Component
public class usersHealthcheck implements HealthIndicator {
	private final String message_key = "Users API";
	RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public Health health() {
		if(!isRunningUsersAPI()) {
			return Health.down().withDetail(message_key, "Not Available").build();
		}
		 return Health.up().withDetail(message_key, "Available").build();
	}
	
	private Boolean isRunningUsersAPI() {
		String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/";
        Boolean isRunning = false;
        try {
			ResponseEntity<List<UserDomain>> usersResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserDomain>>() {
					});
			if (usersResponse != null && usersResponse.hasBody()) {
				List<UserDomain> users = usersResponse.getBody();
				String id = users.get(0).getUserId();
				for (int i = 0; i < users.size(); i++) {
					if (id.equals(users.get(i).getUserId())) {
						isRunning = true;
						break;
					}
				}
			}
		} catch (RestClientException e) {
			return isRunning;
		}
        return isRunning;
    }
}
