package com.devops.manager.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserProject {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JoinColumn(name = "id_user", nullable = false)
    @ManyToOne
    @JsonManagedReference
    private User user;

    @JoinColumn(name = "id_project", nullable = false)
    @ManyToOne
    @JsonManagedReference
    private Project project;

    @Column
    @NotBlank
    @NotNull
    @NotEmpty
    private String role;

    @Column
    @NotBlank
    @NotNull
    @NotEmpty
    private String permission;

}
