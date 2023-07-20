package com.devops.manager.service;

import com.devops.manager.dto.AuthenticationRequest;
import com.devops.manager.dto.UserDto;
import com.devops.manager.model.User;
import org.springframework.stereotype.Service;

@Service
public interface IAuthenticationService {

    UserDto login(AuthenticationRequest authenticationRequest);

    UserDto saveRegistration(User user);

}