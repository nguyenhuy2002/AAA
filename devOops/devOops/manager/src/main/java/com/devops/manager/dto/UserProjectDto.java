package com.devops.manager.dto;

import com.devops.manager.model.UserProject;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserProjectDto {
    private Long id;
    private ProjectDto projectDto;
    private String role;
    private String permission;

    public UserProjectDto() {
    }

    public UserProjectDto(UserProject userProject) {
        this.id = userProject.getId();
        this.projectDto = new ProjectDto(userProject.getProject());
        this.role = userProject.getRole();
        this.permission = userProject.getPermission();
    }

    public static List<ProjectUserDto> fromListUserProject(List<UserProject> userProjects) {
        return userProjects.stream()
                .map(ProjectUserDto::new)
                .collect(Collectors.toList());
    }

    public static Page<ProjectUserDto> fromUserProjects(Page<UserProject> userProjects, Pageable pageable) {
        return new PageImpl<>(
                userProjects.getContent()
                        .stream()
                        .map(ProjectUserDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                userProjects.getTotalElements()
        );
    }
}
