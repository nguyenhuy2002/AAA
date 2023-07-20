package com.devops.manager.service;

import com.devops.manager.dto.ProjectDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IProjectService {

    Page<ProjectDto> findProjectsWithPaginationAndSort(
            Long id,
            String name,
            String repo,
            Long idUser,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    ProjectDto saveProject(ProjectDto projectDto);

    ProjectDto updateProjectInfo(Long projectId, Map<String, Object> project);

    ProjectDto updateProject(Long projectId, ProjectDto projectDto);

    Boolean deleteProject(Long projectId);

    ProjectDto addUserProject(Long projectId, Long userId, Map<String, Object> userProjectInfo);

    ProjectDto updateUserProject(Long projectId, Long userId, Map<String, Object> userProjectInfo);

    ProjectDto deleteUserProject(Long projectId, Long userId);

}