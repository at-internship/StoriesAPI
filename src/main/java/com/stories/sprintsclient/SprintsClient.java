package com.stories.sprintsclient;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.domain.SprintDomain;

@Component
public class SprintsClient {
	
	private static Logger logger = LogManager.getLogger();
	
	RestTemplate restTemplate = new RestTemplate();

	public boolean existsSprintById(String id) throws RestClientException {
		String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
		boolean exists = false;
		try {
			ResponseEntity<List<SprintDomain>> sprintsResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<SprintDomain>>() {
					});

			if (sprintsResponse != null && sprintsResponse.hasBody()) {
				List<SprintDomain> sprints = sprintsResponse.getBody();
				logger.debug("Getting all sprints: " + sprints);
				for (int i = 0; i < sprints.size(); i++) {
					if (id.equals(sprints.get(i).getId())) {
						exists = true;
						logger.debug("Getting sprint if exist: " + sprints.get(i));
						break;
					}
				}
			}
		} catch (RestClientException e) {
			throw new RestClientException("Spritns API has no entities");
		}
		return exists;
	}
}