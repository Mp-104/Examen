package com.example.examen.controller;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.principal.MyPrincipal;
import com.example.examen.repository.PersonnelRepository;
import com.example.examen.service.IPersonnelService;
import com.example.examen.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

@Controller
public class PersonnelController {


    private final IPersonnelService personnelService;
    private final IUserService userService;

    public PersonnelController(IPersonnelService personnelService, IUserService userService) {

        this.personnelService = personnelService;
        this.userService = userService;
    }


    @GetMapping("/personnel")
    public String getPersonalPage (Model model) {

        List<Personnel> personnelList = personnelService.findAll();

        CustomUser loggedInUser = userService.findUserByUsername(getLoggedInUser()).get();
        List<Personnel> usersPersonnelList = loggedInUser.getPersonnelList();

        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", usersPersonnelList);
        return "personnel-page";
    }

    @PostMapping("/personnel")
    public String addPersonnel (@ModelAttribute("personnel") Personnel personnel, Model model) {

        personnelService.savePersonnel(personnel);


        model.addAttribute("added", "Tillagt: " + personnel.getFirstName());
        model.addAttribute("personnel", new Personnel());

        //return "redirect:/personnel";
        return "personnel-page";

    }



}
