package com.example.examen.controller;

import com.example.examen.authorities.UserRole;
import com.example.examen.dto.PersonnelDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.service.IPersonnelService;
import com.example.examen.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final IPersonnelService personnelService;
    private final IUserService userService;


    public AdminController (IPersonnelService personnelService, IUserService userService) {
        this.personnelService = personnelService;
        this.userService = userService;
    }


    @GetMapping("/delete")
    public String deletePage (Model model) {

        model.addAttribute("personnel", new PersonnelDTO(0L));
        return "delete-page";
    }

    @PostMapping("/delete")
    public String deletePersonnel (@ModelAttribute("personnel") PersonnelDTO personnel, Model model) {
        System.out.println("deletePersonnel, personnel.getId(): " + personnel.id());


        model.addAttribute("deleted", personnelService.deletePersonnelById(personnel.id()) );
        return "delete-page";
    }

    @GetMapping("/admin")
    public String toAdmin () {
        return "admin";
    }

    @GetMapping("/admin/users")
    public String showAllUsersPage (Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "userlist-page";
    }

    @GetMapping("/admin/users/{id}")
    public String showUserPage (@PathVariable Long id, Model model) {

        if (userService.findUserById(id).isPresent()) {

            model.addAttribute("user", userService.findUserById(id).get());
            model.addAttribute("roles", UserRole.values());
            return "admin-user-page";
        }

        return "admin-user-page";
    }

    @PostMapping("/admin/users/edit")
    public String editUser (@ModelAttribute("user") CustomUser customUser, Model model) {


        model.addAttribute("user", userService.editUser(customUser));
        model.addAttribute("roles", UserRole.values());
        return "admin-user-page";
    }

}
