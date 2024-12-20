package com.example.examen.controller;

import com.example.examen.model.Personnel;
import com.example.examen.service.IPersonnelService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    private final IPersonnelService personnelService;

    public TestController (IPersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @GetMapping("/test")
    public String testPageable (@RequestParam("page") int page, Model model) {

        Page<Personnel> personnelPage = personnelService.findPersonnelByCountryAllegianceUSA(page,3, "firstName");

        personnelPage.getContent();
        personnelPage.getTotalPages();
        personnelPage.getTotalElements();

        System.out.println("personnelPage.getContent(); " + personnelPage.getContent());
        System.out.println("personnelPage.getTotalPages(); " + personnelPage.getTotalPages());
        System.out.println("personnelPage.getTotalElements(); " + personnelPage.getTotalElements());

        model.addAttribute("content", personnelPage.getContent());


        model.addAttribute("currentPage", personnelPage.getNumber());
        model.addAttribute("totalPages", personnelPage.getTotalPages());
        model.addAttribute("totalElements", personnelPage.getTotalElements());
        return "test-page";

    }
}
