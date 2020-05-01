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

import com.stories.domain.SprintDomain;

@Component
public class sprintsHealthcheck implements HealthIndicator {

	private final String message_key = "Sprints API ";
	RestTemplate restTemplate = new RestTemplate();

	@Override
	public Health health() {
		if (!isRunningUsersAPI()) {
			return Health.down().withDetail(message_key, "Not Available").build();
		}
		return Health.up().withDetail(message_key, "Available").build();
	}

	public Boolean isRunningUsersAPI() {
		String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
		Boolean isRunning = false;
		try {
			ResponseEntity<List<SprintDomain>> sprintsResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<SprintDomain>>() {
					});
			if (sprintsResponse != null & sprintsResponse.hasBody()) {
				isRunning = true;
			}
		} catch (RestClientException e) {
			return isRunning;
		} catch (Exception e) {
			return isRunning;
		}
		return isRunning;
	}
}
