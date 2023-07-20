package com.devops.manager.gitlab;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GitLabArtifactApiClient {
    private static final String GITLAB_API_BASE_URL = "https://gitlab.example.com/api/v4";
    private static final String PRIVATE_TOKEN = "<YOUR_ACCESS_TOKEN>";

    private RestTemplate restTemplate;

    public GitLabArtifactApiClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public void getArtifactsByProjectId(String projectId) {
        String apiUrl = GITLAB_API_BASE_URL + "/projects/{projectId}/jobs/artifacts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        // Add any query parameters if needed

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                projectId,
                queryParams,
                headers
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            // Process the JSON response
            System.out.println(responseBody);
        } else {
            System.out.println("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    // Add more methods for other artifact-related API endpoints as needed

//    public static void main(String[] args) {
//        GitLabArtifactApiClient client = new GitLabArtifactApiClient();
//        String projectId = "<PROJECT_ID>";
//        client.getArtifactsByProjectId(projectId);
//    }
}

