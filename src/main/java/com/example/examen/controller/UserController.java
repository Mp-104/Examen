package com.example.examen.controller;

import com.example.examen.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage () {

        return "login-page";
    }

    @GetMapping("/")
    public String index () {
        return "index";
    }

    @GetMapping("/user")
    public String userPage () {
        return "user-page";
    }



}
