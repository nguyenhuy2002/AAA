package com.devops.manager.gitlab;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class GitLabCICDClient {
    private static final String GITLAB_API_BASE_URL = "https://gitlab.example.com/api/v4";
    private static final String PRIVATE_TOKEN = "<YOUR_ACCESS_TOKEN>";

    private RestTemplate restTemplate;

    public GitLabCICDClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public void getPipelinesByProjectId(String projectId) {
        String apiUrl = GITLAB_API_BASE_URL + "/projects/{projectId}/pipelines";

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

    public void getPipelineById(String projectId, String pipelineId) {
        String apiUrl = GITLAB_API_BASE_URL + "/projects/{projectId}/pipelines/{pipelineId}";

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
                pipelineId,
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

    public void runPipeline(String projectId, String ref) {
        String apiUrl = GITLAB_API_BASE_URL + "/projects/{projectId}/pipeline";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PRIVATE-TOKEN", PRIVATE_TOKEN);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        // Build the request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("ref", ref); // Specify the branch or tag to trigger the pipeline

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class,
                projectId,
                queryParams
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            // Process the JSON response
            System.out.println(responseBody);
        } else {
            System.out.println("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    public static void main(String[] args) {
        GitLabCICDClient client = new GitLabCICDClient();
        String projectId = "<PROJECT_ID>";

        // Get pipelines
        client.getPipelinesByProjectId(projectId);

        // Get pipeline by ID
        String pipelineId = "<PIPELINE_ID>";
        client.getPipelineById(projectId, pipelineId);

        // Run a pipeline
        String ref = "main"; // Specify the branch or tag to trigger the pipeline
        client.runPipeline(projectId, ref);
    }
}
