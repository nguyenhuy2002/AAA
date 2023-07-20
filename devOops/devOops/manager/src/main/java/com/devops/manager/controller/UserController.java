package com.devops.manager.controller;

import com.devops.manager.dto.UserDto;
import com.devops.manager.model.ResponseObject;
import com.devops.manager.model.Role;
import com.devops.manager.model.Sex;
import com.devops.manager.model.User;
import com.devops.manager.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseObject<Page<UserDto>> getUser(@RequestParam(value = "_id", defaultValue = "-1") Long id,
                                                 @RequestParam(value = "_email", defaultValue = "") String email,
                                                 @RequestParam(value = "_name", defaultValue = "") String name,
                                                 @RequestParam(value = "_sex", defaultValue = "") Sex sex,
                                                 @RequestParam(value = "_phone_number", defaultValue = "") String phoneNumber,
                                                 @RequestParam(value = "_role", defaultValue = "") Role role,
                                                 @RequestParam(value = "_page", defaultValue = "0") int page,
                                                 @RequestParam(value = "_limit", defaultValue = "10") int limit,
                                                 @RequestParam(value = "_field", defaultValue = "id") String field,
                                                 @RequestParam(value = "_type_sort", defaultValue = "asc") String typeSort,
                                                 HttpServletRequest request
    ) {
        Page<UserDto> users = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 0);
        try {
            users = userService.findUsersWithPaginationAndSort(id, email, name, sex, phoneNumber, role, page, limit, field, typeSort, request);
            if (users.isEmpty()) {
                return new ResponseObject<>(
                        "failed",
                        "Not found Users.",
                        users
                );
            } else {
                return new ResponseObject<>(
                        "ok",
                        "Get All Successfully.",
                        users
                );
            }
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't get Users with filter. " + exception.getMessage(),
                    users
            );
        }
    }

    @PostMapping
    public ResponseObject<UserDto> addUser(
            @RequestBody User user
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Add User Successfully.",
                    userService.saveUser(user)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't add user. " + exception.getMessage(),
                    new UserDto()
            );
        }
    }

    //admin
    @PutMapping("/{id}")
    public ResponseObject<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody User user,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Update user successfully.",
                    userService.updateUser(id, user, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Can't update user. " + exception.getMessage(),
                    new UserDto()
            );
        }
    }

    @PutMapping("/change_status/{id}")
    public ResponseObject<UserDto> changStatusUser(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Change Status user with id = " + id + " successfully.",
                    userService.changeStatusUser(id, request)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    "Cannot change status User with id = " + id + ". " + exception.getMessage(),
                    new UserDto()
            );
        }
    }

}
