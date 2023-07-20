package com.devops.manager.service.impl;

import com.devops.manager.dto.AuthenticationRequest;
import com.devops.manager.dto.UserDto;
import com.devops.manager.model.User;
import com.devops.manager.service.IAuthenticationService;
import com.devops.manager.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    private final IUserService userService;


    @Override
    public UserDto login(AuthenticationRequest authenticationRequest) {
        log.info("Login for user {}", authenticationRequest);
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        User user = userService.findUserByUsername(username).orElseThrow(() -> new RuntimeException("No account found matching username."));
        if (!user.isStatus()) {
            throw new RuntimeException("User is disabled.");
        } else if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return new UserDto(user);
        } else
            throw new RuntimeException("Your password is incorrect. Please re-enter your password.");
    }


    @Override
    public UserDto saveRegistration(User user) {
        log.info("Saving registration for user {}", user);
        return userService.registerUser(user);
    }

}