package com.devops.manager.dto;

import com.devops.manager.model.Role;
import com.devops.manager.model.Sex;
import com.devops.manager.model.User;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String phoneNumber;
    private boolean status = true;
    private Role role;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.dateOfBirth = user.getDateOfBirth();
        this.sex = user.getSex();
        this.phoneNumber = user.getPhoneNumber();
        this.status = user.isStatus();
        this.role = user.getRole();
    }

    public static List<UserDto> fromListUser(List<User> users) {
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public static Page<UserDto> fromUsers(Page<User> users, Pageable pageable) {
        return new PageImpl<>(
                users.getContent()
                        .stream()
                        .map(UserDto::new)
                        .collect(Collectors.toList()
                        ),
                pageable,
                users.getTotalElements()
        );
    }
}
