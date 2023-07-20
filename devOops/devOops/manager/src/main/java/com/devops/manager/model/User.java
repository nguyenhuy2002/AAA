package com.devops.manager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 40)
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 10)
    private String lastName;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Builder.Default
    private String password = new BCryptPasswordEncoder().encode("Abcd@1234");

    @NotBlank
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotBlank
    @Column(unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean status = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @JsonBackReference("user-projects")
    private List<UserProject> userProjects;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email) || phoneNumber.equals(user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return (email + phoneNumber).hashCode();
    }

}