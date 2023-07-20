package com.devops.manager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "projects")
public class Project {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    @Column
    private String description;

    @Column
    @NotBlank
    private String repo;

    @NotBlank
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    @JsonBackReference("project-users")
    private List<UserProject> userProjects;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", repo='" + repo + '\'' +
                ", createdDate=" + createdDate +
                ", userProjects=" + userProjects +
                '}';
    }
}