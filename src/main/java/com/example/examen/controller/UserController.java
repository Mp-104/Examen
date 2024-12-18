package com.example.examen.controller;

import com.example.examen.authorities.UserRole;
import com.example.examen.dto.PasswordDT0;
import com.example.examen.dto.UserDTO;
import com.example.examen.principal.MyPrincipal;
import com.example.examen.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

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
    public String userPage (Model model) {

        model.addAttribute("user", userService.findUserByUsername(getLoggedInUser()).get());
        return "user-page";
    }

    @PostMapping("/user")
    public String disableUser (HttpServletRequest request, HttpServletResponse response) {

        userService.disableUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/changePassword")
    public String changePassword (Model model) {

        model.addAttribute("password", new PasswordDT0("", ""));
        return "password-page";
    }

    @PostMapping("/changePassword")
    public String changePassword (@Valid @ModelAttribute("password") PasswordDT0 password, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "password-page";
        }


        model.addAttribute("status", userService.changePassword(password));
        model.addAttribute("password", new PasswordDT0("", ""));
        return "password-page";
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
