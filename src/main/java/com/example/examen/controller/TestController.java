package com.example.examen.controller;

import com.example.examen.model.Personnel;
import com.example.examen.service.IPersonnelService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.examen.countries.NatoCountries.getAllNatoCountries;

@Controller
public class TestController {

    private final IPersonnelService personnelService;

    public TestController (IPersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @GetMapping("/test1")
    public String testPageable (@RequestParam(value = "page" , required = false, defaultValue = "0") int page,
                                @RequestParam(value = "country", required = false, defaultValue = "ALL") String country,
                                Model model) throws InstantiationException, IllegalAccessException {



        if (!Objects.equals(country, "ALL")) {
            Page<Personnel> personnelPage = personnelService.findPersonnelByCountryAllegiance(country, page,3, "firstName");

            model.addAttribute("content", personnelPage.getContent());
            model.addAttribute("currentPage", personnelPage.getNumber());
            model.addAttribute("totalPages", personnelPage.getTotalPages());
            model.addAttribute("totalElements", personnelPage.getTotalElements());

            model.addAttribute("selectedCountry", country);
            return "test-page";
        }
        Pageable pageable = PageRequest.of(page, 3, Sort.by("firstName"));
        Page<Personnel> allPersonnelPage = personnelService.findAllPersonnel(pageable);

        model.addAttribute("content", allPersonnelPage.getContent());
        model.addAttribute("currentPage", allPersonnelPage.getNumber());
        model.addAttribute("totalPages", allPersonnelPage.getTotalPages());
        model.addAttribute("selectedCountry", country);

        return "test-page";

    }

    @GetMapping("/getPersonnel")
    public String testPageable1 (@RequestParam(value = "page" , required = false, defaultValue = "0") int page,
                                @RequestParam(value = "country", required = false, defaultValue = "ALL") String country,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize,
                                Model model) throws InstantiationException, IllegalAccessException {

        List<Personnel> allPersonnel = personnelService.findAll().stream().sorted(Comparator.comparing(Personnel::getFirstName)).collect(Collectors.toList());
        List<Personnel> personnelByCountry = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("firstName").ascending());

        Page<Personnel> page1 = personnelService.paginateList(allPersonnel, pageable);

        if (!Objects.equals(country, "ALL")) {


            for (Personnel personnel : allPersonnel) {
                if (Objects.equals(personnel.getCountryAllegiance(), country)) {
                    personnelByCountry.add(personnel);
                }
            }

            Page<Personnel> page2 = personnelService.paginateList(personnelByCountry, pageable);

            //Page<Personnel> personnelPage = personnelService.findPersonnelByCountryAllegiance(country, page,3, "firstName");


            model.addAttribute("content", page2.getContent());
            model.addAttribute("currentPage", page2.getNumber());
            model.addAttribute("totalPages", page2.getTotalPages());
            model.addAttribute("totalElements", page2.getTotalElements());

            model.addAttribute("selectedCountry", country);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("countries", getAllNatoCountries());


            return "test-page";
        }


        model.addAttribute("content", page1.getContent());
        model.addAttribute("currentPage", page1.getNumber());
        model.addAttribute("totalPages", page1.getTotalPages());
        model.addAttribute("selectedCountry", country);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("countries", getAllNatoCountries());


        return "test-page";

    }



    @GetMapping("/testpersonnel")
    public String listPersonnel (@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Personnel> personnelPage = personnelService.findAllPersonnel(pageable);

        model.addAttribute("personnelPage", personnelPage);

        return "test-personnel-page";

    }
}
