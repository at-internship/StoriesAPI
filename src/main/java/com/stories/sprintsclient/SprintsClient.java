package com.stories.sprintsclient;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.stories.domain.SprintDomain;

@Component
public class SprintsClient {

	public boolean existsSprintById(String id) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
		boolean exists = false;
		try {
			ResponseEntity<List<SprintDomain>> sprintsResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<SprintDomain>>() {
					});

			if (sprintsResponse != null && sprintsResponse.hasBody()) {
				List<SprintDomain> sprints = sprintsResponse.getBody();
				for (int i = 0; i < sprints.size(); i++) {
					if (id.equals(sprints.get(i).getId())) {

						exists = true;
						break;
					}
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return exists;
	}

}
