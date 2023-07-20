package com.devops.manager.dto;

import com.devops.manager.github.GitHubCICDClient;
import com.devops.manager.model.Project;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ProjectDto {
    private Long id;
    private String name;
    private String repo;
    private String description;
    private Map workflows;
    private LocalDateTime createdDate;
    private List<ProjectUserDto> projectUserDto;

    public ProjectDto() {
    }

    public ProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.repo = project.getRepo();
        this.description = project.getDescription();
        this.createdDate = project.getCreatedDate();
        this.projectUserDto = ProjectUserDto.fromListUserProject(project.getUserProjects());
    }

    public ProjectDto(Project project, int type) {
        this.id = project.getId();
        this.name = project.getName();
        this.repo = project.getRepo();
        this.description = project.getDescription();
        if (type == 1) {
            String[] r = repo.split("/");
            this.workflows = new GitHubCICDClient().getWorkflowsByRepo(r[r.length - 2], r[r.length - 1]);
        }
        this.createdDate = project.getCreatedDate();
        this.projectUserDto = ProjectUserDto.fromListUserProject(project.getUserProjects());
    }

    public static List<ProjectDto> fromListProject(List<Project> projects) {
        return projects.stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    public static Page<ProjectDto> fromProjects(Page<Project> projects, int type, Pageable pageable) {
        return new PageImpl<>(
                projects.getContent()
                        .stream()
                        .map(p -> new ProjectDto(p, type))
                        .collect(Collectors.toList()),
                pageable,
                projects.getTotalElements());
    }
}
