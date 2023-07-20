package com.devops.manager.service;

import com.devops.manager.dto.UserDto;
import com.devops.manager.model.Role;
import com.devops.manager.model.Sex;
import com.devops.manager.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserService {
    
    Page<UserDto> findUsersWithPaginationAndSort(
            Long id,
            String email,
            String name,
            Sex sex,
            String phoneNumber,
            Role role,
            int page,
            int limit,
            String field,
            String typeSort,
            HttpServletRequest request);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    UserDto registerUser(User user);

    UserDto saveUser(User user);


    UserDto updateUser(Long id, User user, HttpServletRequest request);


    UserDto changeStatusUser(Long userId, HttpServletRequest request);

    void userInitialization();


}