package com.devops.manager.controller;

import com.devops.manager.github.GitHubCICDClient;
import com.devops.manager.model.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Controller
@RequestMapping(path = "/api/cicd")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GitHubCICDController {
    private final GitHubCICDClient gitHubCICDClient = new GitHubCICDClient();

    @GetMapping("/getWorkflowsByRepo/{owner}/{repo}")
    public ResponseObject<Map> getProject(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            HttpServletRequest request
    ) {
        Map workflows = new HashMap<>();
        try {
            workflows = gitHubCICDClient.getWorkflowsByRepo(owner, repo);
            if (workflows.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found.",
                        workflows);
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        workflows);
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get with filter. " + exception.getMessage(),
                    workflows);
        }
    }

    @GetMapping("/getWorkflowRunsByRepo/{owner}/{repo}")
    public ResponseObject<Map> getWorkflowRunsByRepo(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            HttpServletRequest request
    ) {
        Map workflowRuns = new HashMap<>();
        try {
            workflowRuns = gitHubCICDClient.getWorkflowRunsByRepo(owner, repo);
            if (workflowRuns.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found.",
                        workflowRuns);
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        workflowRuns);
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get with filter. " + exception.getMessage(),
                    workflowRuns);
        }
    }

    @GetMapping("/lisWorkflowRunsForAWorkflow/{owner}/{repo}/{workFlowId}")
    public ResponseObject<Map> lisWorkflowRunsForAWorkflow(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @PathVariable("workFlowId") String workFlowId,
            HttpServletRequest request
    ) {
        Map workflowRuns = new HashMap<>();
        try {
            workflowRuns = gitHubCICDClient.lisWorkflowRunsForAWorkflow(owner, repo, workFlowId);
            if (workflowRuns.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found.",
                        workflowRuns);
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        workflowRuns);
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get with filter. " + exception.getMessage(),
                    workflowRuns);
        }
    }
}
