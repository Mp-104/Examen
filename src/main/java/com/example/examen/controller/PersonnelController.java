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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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


        CustomUser loggedInUser = userService.findUserByUsername(
                getLoggedInUser()
                //"test"
        ).get();
        List<Personnel> usersPersonnelList = loggedInUser.getPersonnelList();

//        Personnel firstPersonnel = usersPersonnelList.get(0);
//
//        model.addAttribute("firstPersonnel", firstPersonnel);



        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", usersPersonnelList);
        return "personnel-page";
    }

    @PostMapping("/personnel")
    public String addPersonnel (@ModelAttribute("personnel") Personnel personnel,
                                Model model,
                                @RequestParam("imageFile") MultipartFile file
    ) throws IOException {

        //Byte[] byteObjects = convertToBytes(file);

        personnel.setPicture(file.getBytes());

        personnelService.savePersonnel(personnel);


        model.addAttribute("added", "Tillagt: " + personnel.getFirstName());
        model.addAttribute("personnel", new Personnel());

        //return "redirect:/personnel";
        return "personnel-page";

    }

    public Byte[] convertToBytes (MultipartFile file) throws IOException {
        Byte[] byteObjects = new Byte[file.getBytes().length];

        int i = 0;

        for(byte b : file.getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }



}
