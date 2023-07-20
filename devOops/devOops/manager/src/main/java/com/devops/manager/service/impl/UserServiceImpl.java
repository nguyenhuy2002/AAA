package com.devops.manager.service.impl;

import com.devops.manager.dto.UserDto;
import com.devops.manager.model.Role;
import com.devops.manager.model.Sex;
import com.devops.manager.model.User;
import com.devops.manager.repository.UserRepository;
import com.devops.manager.service.IUserService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EntityManager entityManager;

    @Override
    public Page<UserDto> findUsersWithPaginationAndSort(
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
            HttpServletRequest request) {
        LinkedHashMap<String, Object> filter = new LinkedHashMap<String, Object>();
        filter.put("id", id);
        filter.put("email", email);
        filter.put("name", name);
        filter.put("sex", sex);
        filter.put("phoneNumber", phoneNumber);
        filter.put("role", role);
        filter.put("page", page);
        filter.put("limit", limit);
        filter.put("field", field);
        filter.put("typeSort", typeSort);
        log.info("Finding users with pagination and sort " + filter);
        Pageable pageable = PageRequest.of(page, limit)
                .withSort(Sort.by(typeSort.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        return UserDto.fromUsers(userRepository.findUsersWithPaginationAndSort(id, email, name, sex,
                phoneNumber, role, pageable), pageable);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        log.info("Finding user by email: {}", email);
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        log.info("Finding user by username: {}", username);
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error when querying.");
        }
    }

    @Override
    public UserDto registerUser(User user) {
        log.info("Register user: {}", user);
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        try {
            return new UserDto(userRepository.save(user));
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Email or PhoneNumber already exists, please enter another to continue.");
        }
    }

    @Override
    public UserDto saveUser(User user) {
        log.info("Saving user: {}", user);
        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?=\\S+$).{8,20}$"))
            throw new RuntimeException(
                    "Invalid password. Password must be between 8 and 20 characters and include at least one uppercase letter, one lowercase letter, one number and one special character in the following @#$%^&+=_!");
        try {
            return new UserDto(
                    userRepository.save(
                            User.builder()
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                                    .sex(user.getSex())
                                    .dateOfBirth(user.getDateOfBirth())
                                    .phoneNumber(user.getPhoneNumber())
                                    .status(user.isStatus())
                                    .role(user.getRole())
                                    .build()));
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException("Email or PhoneNumber already exists, please enter another to continue.");
        }
    }

    @Override
    public UserDto updateUser(Long id, User user, HttpServletRequest request) {
        log.info("Updating user by admin: {}", id);
//        if (id == 1)
//            throw new RuntimeException("You cannot edit the system default account.");
//        else
        return userRepository.findById(id).map(updateUser -> {
            if (!Objects.equals(updateUser.getEmail(), user.getEmail())
                    && userRepository.findByEmail(user.getEmail()).isPresent())
                throw new RuntimeException("Email already exists, please enter another email to continue.");
            if (!Objects.equals(updateUser.getPhoneNumber(), user.getPhoneNumber())
                    && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent())
                throw new RuntimeException("Phone number already exists, please enter another phone number to continue.");
            String newPassword = !Objects.equals(user.getPassword(), updateUser.getPassword()) ? new BCryptPasswordEncoder().encode(user.getPassword()) : updateUser.getPassword();
            return new UserDto(
                    userRepository.save(
                            User.builder()
                                    .id(id)
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .password(newPassword)
                                    .sex(user.getSex())
                                    .dateOfBirth(user.getDateOfBirth())
                                    .phoneNumber(user.getPhoneNumber())
                                    .status(user.isStatus())
                                    .role(user.getRole())
                                    .build()));
        }).orElseThrow(() -> new RuntimeException("User not found."));
    }


    @Override
    public UserDto changeStatusUser(Long userId, HttpServletRequest request) {
        log.info("Change Status User " + userId);
        if (userId == 1)
            throw new RuntimeException("You cannot edit the system default account.");
        else
            return userRepository.findById(userId).map(user -> {
                user.setStatus(!user.isStatus());
                return new UserDto(userRepository.save(user));
            }).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Override
    public void userInitialization() {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            log.info("Initializing users");
            try {
                userRepository.save(
                        User.builder()
                                .firstName("Admin")
                                .lastName("Admin")
                                .email("admin@gmail.com")
                                .password(new BCryptPasswordEncoder().encode("admin_Abcd@1234"))
                                .sex(Sex.MALE)
                                .dateOfBirth(LocalDate.now())
                                .phoneNumber("0370123454")
                                .status(true)
                                .role(Role.ADMIN)
                                .build());
            } catch (Exception e) {
                throw new RuntimeException("Error when querying.");
            }
        }
    }
}