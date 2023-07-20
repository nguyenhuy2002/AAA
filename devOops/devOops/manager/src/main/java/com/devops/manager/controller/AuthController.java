package com.devops.manager.controller;

import com.devops.manager.dto.AuthenticationRequest;
import com.devops.manager.dto.UserDto;
import com.devops.manager.model.ResponseObject;
import com.devops.manager.model.User;
import com.devops.manager.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private final IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseObject<UserDto> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Login Successfully.",
                    authenticationService.login(authenticationRequest)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    exception.getMessage(),
                    new UserDto()
            );
        }
    }


    @PostMapping("/register")
    public ResponseObject<UserDto> registration(@RequestBody User user) {
        try {
            return new ResponseObject<>(
                    "ok",
                    "Register Successfully.",
                    authenticationService.saveRegistration(user)
            );
        } catch (Exception exception) {
            return new ResponseObject<>(
                    "error",
                    exception.getMessage(),
                    new UserDto()
            );
        }
    }
}
