package com.devops.manager.service.impl;

import com.devops.manager.dto.ProjectDto;
import com.devops.manager.model.Project;
import com.devops.manager.model.UserProject;
import com.devops.manager.repository.ProjectRepository;
import com.devops.manager.repository.UserProjectRepository;
import com.devops.manager.repository.UserRepository;
import com.devops.manager.service.IProjectService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectServiceImpl implements IProjectService {
    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final UserProjectRepository userProjectRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EntityManager entityManager;

    @Override
    public Page<ProjectDto> findProjectsWithPaginationAndSort(
            Long id,
            String name,
            String repo,
            Long idUser,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        return ProjectDto.fromProjects(
                projectRepository.findProjectsWithPaginationAndSort(id, name, repo, idUser, pageable),
                id != -1 ? 1 : 0,
                pageable);
    }

    @Override
    public ProjectDto saveProject(ProjectDto projectDto) {
        log.info("Saving project: {}", projectDto);
        try {
            Project project = projectRepository.save(
                    Project.builder()
                            .name(projectDto.getName())
                            .repo(projectDto.getRepo())
                            .description(projectDto.getDescription())
                            .createdDate(LocalDateTime.now())
                            .build());
            System.out.println(project);
            System.out.println("=======================================");
            System.out.println(projectDto.getProjectUserDto());
            List<UserProject> userProjects = userProjectRepository.saveAll(projectDto.getProjectUserDto()
                    .stream()
                    .map((u) -> UserProject.builder()
                            .project(project)
                            .user(userRepository.findById(u.getUserDto().getId())
                                    .orElseThrow(() -> new RuntimeException("Not Found User")))
                            .role(u.getRole())
                            .permission(u.getPermission())
                            .build())
                    .collect(Collectors.toList()));
            project.setUserProjects(userProjects);
            return new ProjectDto(project);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Email or PhoneNumber already exists, please enter another to continue.");
        }
    }

    @Override
    public ProjectDto updateProjectInfo(Long projectId, Map<String, Object> project) {
        log.info("Updating user by admin: {}", projectId);
        return projectRepository.findById(projectId).map(updateProject -> {
            updateProject.setName(project.get("name").toString());
            updateProject.setRepo(project.get("repo").toString());
            updateProject.setDescription(project.get("description").toString());
            return new ProjectDto(
                    projectRepository.save(updateProject));
        }).orElseThrow(() -> new RuntimeException("Project not found."));
    }

    @Override
    public ProjectDto updateProject(Long projectId, ProjectDto projectDto) {
        log.info("Updating user by admin: {}", projectId);
        return projectRepository.findById(projectId).map(updateProject -> {
            updateProject.setName(projectDto.getName());
            updateProject.setDescription(projectDto.getDescription());
            updateProject.setRepo(projectDto.getRepo());
            List<Long> listProjectId = new ArrayList<>();
            List<UserProject> uPUpdate = projectDto.getProjectUserDto()
                    .stream()
                    .map((u) -> {
                        UserProject userProject = UserProject.builder()
                                .project(updateProject)
                                .user(userRepository.findById(u.getUserDto().getId())
                                        .orElseThrow(() -> new RuntimeException("Not Found User")))
                                .role(u.getRole())
                                .permission(u.getPermission())
                                .build();
                        if (u.getId() != null) {
                            listProjectId.add(u.getId());
                            userProject.setId(u.getId());
                        }
                        return userProject;
                    })
                    .collect(Collectors.toList());
            userProjectRepository.deleteAllNotInListId(projectId, listProjectId);
            List<UserProject> userProjects = userProjectRepository.saveAll(uPUpdate);
            Project updatedProject = projectRepository.save(updateProject);
            updatedProject.setUserProjects(userProjects);
            return new ProjectDto(updatedProject);
        }).orElseThrow(() -> new RuntimeException("Project not found."));
    }

    @Override
    public Boolean deleteProject(Long projectId) {
        try {
            userProjectRepository.deleteAll(userProjectRepository
                    .findAll()
                    .stream()
                    .filter(p -> Objects.equals(p.getProject().getId(), projectId)).collect(Collectors.toList()));
            projectRepository.deleteById(projectId);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new RuntimeException("Error when querying");
        }
    }

    @Override
    public ProjectDto addUserProject(Long projectId, Long userId, Map<String, Object> userProjectInfo) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Not found Project"));
        project.getUserProjects().add(UserProject.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found User")))
                .role(userProjectInfo.get("role").toString())
                .permission(userProjectInfo.get("permission").toString())
                .build());

        return new ProjectDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto updateUserProject(Long projectId, Long userId, Map<String, Object> userProjectInfo) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Not found Project"));
        UserProject userProject = userProjectRepository
                .findAll()
                .stream()
                .filter(p -> Objects.equals(p.getProject().getId(), projectId)
                        && Objects.equals(p.getUser().getId(), userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project Not Found"));
        userProject.setRole(userProjectInfo.get("role").toString());
        userProject.setPermission(userProjectInfo.get("permission").toString());

        // project.getUserProjects().add(UserProject.builder()
        // .user(userRepository.findById(userId).orElseThrow(() -> new
        // RuntimeException("Not found User")))
        // .role(role)
        // .permission(permission)
        // .build());
        project.getUserProjects().add(userProject);
        return new ProjectDto(project);
    }

    @Override
    public ProjectDto deleteUserProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Not found Project"));
        project.setUserProjects(project.getUserProjects().stream()
                .filter(p -> !Objects.equals(p.getUser().getId(), userId)).collect(Collectors.toList()));
        return new ProjectDto(projectRepository.save(project));
    }

}