package com.devops.manager.controller;

import com.devops.manager.dto.ProjectDto;
import com.devops.manager.model.ResponseObject;
import com.devops.manager.service.IProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectController {
    private final IProjectService projectService;

    @GetMapping
    public ResponseObject<Page<ProjectDto>> getProject(@RequestParam(value = "_id", defaultValue = "-1") Long id,
                                                       @RequestParam(value = "_name", defaultValue = "") String name,
                                                       @RequestParam(value = "_repo", defaultValue = "") String repo,
                                                       @RequestParam(value = "_id_user", defaultValue = "-1") Long idUser,
                                                       @RequestParam(value = "_page", defaultValue = "0") int page,
                                                       @RequestParam(value = "_limit", defaultValue = "10") int limit,
                                                       @RequestParam(value = "_field", defaultValue = "id") String field,
                                                       @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
                                                       HttpServletRequest request) {
        Page<ProjectDto> projects = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            projects = projectService.findProjectsWithPaginationAndSort(id, name, repo, idUser, page, limit, field,
                    typeSort, request);
            if (projects.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Users.",
                        projects);
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        projects);
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get with filter. " + exception.getMessage(),
                    projects);
        }
    }

    @PostMapping
    public ResponseObject<ProjectDto> addProject(
            @RequestBody ProjectDto projectDto) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add Successfully.",
                    projectService.saveProject(projectDto));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add. " + exception.getMessage(),
                    new ProjectDto());
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<ProjectDto> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDto projectDto,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update successfully.",
                    projectService.updateProject(id, projectDto));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update. " + exception.getMessage(),
                    new ProjectDto());
        }
    }

    @PutMapping("/update-info/{id}")
    public ResponseObject<ProjectDto> updateProjectInfo(
            @PathVariable Long id,
            @RequestBody Map<String, Object> projectInfo,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update successfully.",
                    projectService.updateProjectInfo(id, projectInfo));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update. " + exception.getMessage(),
                    new ProjectDto());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Boolean> deleteProject(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update successfully.",
                    projectService.deleteProject(id));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update. " + exception.getMessage(),
                    Boolean.FALSE);
        }
    }

    @PostMapping("{projectId}/add-user-to-project/{userId}")
    public ResponseObject<ProjectDto> addUserToProject(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, Object> userProjectInfo,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add user to project successfully.",
                    projectService.addUserProject(projectId, userId, userProjectInfo));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add user to project. " + exception.getMessage(),
                    new ProjectDto());
        }
    }

    @PutMapping("{projectId}/update-user-to-project/{userId}")
    public ResponseObject<ProjectDto> updateUserToProject(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, Object> userProjectInfo,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update user to project successfully.",
                    projectService.updateUserProject(projectId, userId, userProjectInfo));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update user to project. " + exception.getMessage(),
                    new ProjectDto());
        }
    }

    @DeleteMapping("{projectId}/delete-user-to-project/{userId}")
    public ResponseObject<ProjectDto> deleteUserToProject(
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId,
            HttpServletRequest request) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Delete user to project successfully.",
                    projectService.deleteUserProject(projectId, userId));
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't delete user to project. " + exception.getMessage(),
                    new ProjectDto());
        }
    }
}
