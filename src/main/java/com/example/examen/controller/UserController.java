package com.example.examen.controller;

import com.example.examen.authorities.UserRole;
import com.example.examen.dto.UserDTO;
import com.example.examen.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/register")
    public String registerPage (Model model) {

        model.addAttribute("newUser", new UserDTO("","", "", "", UserRole.USER));
        return "register-page";
    }

    @PostMapping("/register")
    public String registerUser (@Valid @ModelAttribute("newUser") UserDTO newUser,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("newUser", newUser);
            return "register-page";
        }

        System.out.println("Registrerar användare...");
        System.out.println("Förnamn: " + newUser.firstName());
        System.out.println("Efternamn: " + newUser.lastName());
        System.out.println("Användare registrerad!");

        model.addAttribute("status", userService.saveUser(newUser));
        model.addAttribute("newUser", new UserDTO("", "", "", "", UserRole.USER));
        return "register-page";
    }

}
