package com.example.examen.controller;

import com.example.examen.model.Personnel;
import com.example.examen.repository.PersonnelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PersonnelController {

    private final PersonnelRepository personnelRepository;

    public PersonnelController(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }


    @GetMapping("/personnel")
    public String getPersonalPage (Model model) {

        List<Personnel> personnelList = personnelRepository.findAll();

        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", personnelList);
        return "personnel-page";
    }

    @PostMapping("/personnel")
    public String addPersonnel (@ModelAttribute("personnel") Personnel personnel, Model model) {

        personnelRepository.save(personnel);


        model.addAttribute("added", "Tillagt: " + personnel.getFirstName());
        model.addAttribute("personnel", new Personnel());

        //return "redirect:/personnel";
        return "personnel-page";

    }



}
