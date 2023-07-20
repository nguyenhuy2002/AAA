package com.devops.manager;

import com.devops.manager.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ManagerApplication implements CommandLineRunner {
    @Autowired
    private final IUserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.userInitialization();
    }

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

}
