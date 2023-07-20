package com.devops.manager.github;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GitHubCICDClient {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String ACCESS_TOKEN = "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";

    private RestTemplate restTemplate;

    public GitHubCICDClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    //////////////////// WORKFLOW ////////////////////////
    public Map getWorkflowsByRepo(String owner, String repo) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                headers
        );
        return response(response);
    }

    public Map getWorkflowById(String owner, String repo, String workflowId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(workflowId),
                headers
        );
        return response(response);
    }

    public Map getWorkflowUsage(String owner, String repo, String workflowId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId) + "/timing";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(workflowId),
                headers
        );
        return response(response);
    }

    public Map disableWorkflowById(String owner, String repo, String workflowId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId) + "/disable";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                null,
                String.class,
                owner,
                repo,
                convertId(workflowId),
                headers
        );
        return response(response);
    }

    public Map enableWorkflowById(String owner, String repo, String workflowId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId) + "/enable";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                null,
                String.class,
                owner,
                repo,
                convertId(workflowId),
                headers
        );
        return response(response);
    }

    public Map runWorkflow(String owner, String repo, String workflowId, String ref) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId) + "/dispatches";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        // Build the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ref", ref);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class,
                owner,
                repo,
                convertId(workflowId)
        );

        return response(response);
    }


    /////////////////////////// ARTIFACTS ///////////////////////////
    public Map getAtifacts(String owner, String repo) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/{owner}/{repo}/actions/artifacts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("per_page", 100); // Số lượng artifacts trên mỗi trang

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(owner, repo).toUri(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response(response);
    }

    public Map getArtifactById(String owner, String repo, String artifactId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/{owner}/{repo}/actions/artifacts/{artifactId}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("per_page", 100); // Số lượng artifacts trên mỗi trang

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(owner, repo, convertId(artifactId)).toUri(),
                HttpMethod.GET,
                entity,
                String.class
        );
        return response(response);
    }

    public Map getArtifactsForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/{owner}/{repo}/actions/runs/{runId}/artifacts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("per_page", 100); // Số lượng artifacts trên mỗi trang

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(owner, repo, convertId(runId)).toUri(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response(response);
    }

    public Map deleteArtifactById(String owner, String repo, String artifactId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/{owner}/{repo}/actions/artifacts/{artifactId}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("per_page", 100); // Số lượng artifacts trên mỗi trang

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(owner, repo, convertId(artifactId)).toUri(),
                HttpMethod.DELETE,
                entity,
                String.class
        );

        return response(response);
    }

    public Map getArtifactsByJob(String owner, String repo, String jobId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/4989919404/jobs/" + convertId(jobId) + "/artifacts";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("per_page", 100); // Số lượng artifacts trên mỗi trang

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.buildAndExpand(owner, repo, convertId(jobId)).toUri(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response(response);
    }

    public String downloadArtifact(String owner, String repo, String artifactId, String archiveFormat) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/artifacts/" + convertId(artifactId) + "/" + archiveFormat;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class,
                owner,
                repo,
                convertId(artifactId)
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] artifactBytes = response.getBody();

            String filename = UUID.randomUUID() + ".zip";
            try (OutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(artifactBytes);
                System.out.println("Artifact downloaded and saved to: " + filename);
                return filename;
            } catch (IOException e) {
                throw new RuntimeException("Cannot save file: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    //////////////////////// WORKFLOW JOB ///////////////////
    public Map getAJobForAWorkflowRun(String owner, String repo, String jobId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/jobs/" + convertId(jobId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(jobId),
                headers
        );
        return response(response);
    }

    public String downloadJobLogsForAWorkflowRun(String owner, String repo, String jobId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/artifacts/" + convertId(jobId) + "/logs";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class,
                owner,
                repo,
                convertId(jobId)
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] logsBytes = response.getBody();

            String filename = UUID.randomUUID() + ".zip";
            try (OutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(logsBytes);
                System.out.println("Artifact downloaded and saved to: " + filename);
                return filename;
            } catch (IOException e) {
                throw new RuntimeException("Cannot save file: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    public Map getJobsForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/jobs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map getJobsForAWorkflowRunAttempt(String owner, String repo, String runId, String attemptNumber) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/attempts/" + attemptNumber + "/jobs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                attemptNumber,
                headers
        );
        return response(response);
    }

    /////////////////////// WORKFLOW RUNS ///////////////////
    public Map reRunAJobFromWorkflowRun(String owner, String repo, String jobId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/jobs/" + convertId(jobId) + "/rerun";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(jobId),
                headers
        );
        return response(response);
    }

    public Map getWorkflowRunsByRepo(String owner, String repo) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                headers
        );
        return response(response);
    }

    public Map getAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map deleteAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.DELETE,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map getReviewHistoryForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/approvals";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map approveAWorkflowRunForAForkPullRequest(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/approve";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map getAWorkflowRunAttempt(String owner, String repo, String runId, String attemptNumber) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/attempts/" + attemptNumber + "/jobs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                attemptNumber,
                headers
        );
        return response(response);
    }

    public String downloadWorkflowRunAttemptLogs(String owner, String repo, String runId, String attemptNumber) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/attempts/" + attemptNumber + "/logs";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class,
                owner,
                repo,
                convertId(runId),
                attemptNumber
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] logsBytes = response.getBody();

            String filename = UUID.randomUUID() + ".zip";
            try (OutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(logsBytes);
                System.out.println("Artifact downloaded and saved to: " + filename);
                return filename;
            } catch (IOException e) {
                throw new RuntimeException("Cannot save file: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    public Map cancelAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map reviewCustomDeploymentProtectionRulesForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/deployment_protection_rule";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public String downloadWorkflowRunLogs(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/logs";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class,
                owner,
                repo,
                convertId(runId)
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] logsBytes = response.getBody();

            String filename = UUID.randomUUID() + ".zip";
            try (OutputStream outputStream = new FileOutputStream(filename)) {
                outputStream.write(logsBytes);
                System.out.println("Artifact downloaded and saved to: " + filename);
                return filename;
            } catch (IOException e) {
                throw new RuntimeException("Cannot save file: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    public Map deleteWorkflowRunLogs(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/logs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.DELETE,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map getPendingDeploymentsForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/pending_deployments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map reviewPendingDeploymentsForAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/pending_deployments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map reRunAWorkflow(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/rerun";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map reRuFailedJobsFromAWorkflowRun(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/rerun-failed-jobs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map getWorkflowRunUsage(String owner, String repo, String runId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/runs/" + convertId(runId) + "/timing";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                convertId(runId),
                headers
        );
        return response(response);
    }

    public Map lisWorkflowRunsForAWorkflow(String owner, String repo, String workflowId) {
        String apiUrl = GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/actions/workflows/" + convertId(workflowId) + "/runs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                String.class,
                owner,
                repo,
                workflowId,
                headers
        );
        return response(response);
    }

    //////////////////////// RESPONSE ///////////////////////
    public Map response(ResponseEntity<String> response) {
        Gson gson = new Gson();
        if (response.getStatusCode().is2xxSuccessful()) {
            return gson.fromJson(response.getBody(), Map.class);
        } else {
            throw new RuntimeException("Request failed with status code: " + response.getStatusCodeValue());
        }
    }

    private static Long convertId(String id) {
        return (long) Double.parseDouble(id);
    }

    public static void main(String[] args) {
        GitHubCICDClient client = new GitHubCICDClient();
        String owner = "nguyenhuy2002";
        String repo = "cicd";
//        System.out.println(client.getWorkflowsByRepo(owner, repo));
//        System.out.println(convertId("5.7339709E7"));
//        System.out.println(client.lisWorkflowRunsForAWorkflow(owner, repo, "5.7339709E7"));
        System.out.println(new BCryptPasswordEncoder().encode("admin_Abcd@1234"));

    }
}

