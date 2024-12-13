package com.example.examen.controller;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.principal.MyPrincipal;
import com.example.examen.repository.PersonnelRepository;
import com.example.examen.service.IPersonnelService;
import com.example.examen.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
                                @RequestParam("imageFiles") List<MultipartFile> files,
                                @RequestParam("imageFile") MultipartFile file // Make sure the template form has: enctype="multipart/form-data", which allows the form to be sent as a Multipart file with a Personnel object and image file
    ) throws IOException {

        // Todo - clean up and move to service class

        List<byte[]> pictures = new ArrayList<>();
        // Checks to see if it's empty
        if (!Arrays.toString(files.get(0).getBytes()).equals("[]")) {

            for (MultipartFile file1 : files) {
                pictures.add(file1.getBytes());
            }
            System.out.println("-----In Controller addPersonnel ()-------");
//            System.out.println("files.size: " + files.size());
//            System.out.println("files.get(0): " + files.get(0));
//            System.out.println("Arrays.toStringfiles.get(0).getBytes: " + Arrays.toString(files.get(0).getBytes()));
            System.out.println("-----In Controller addPersonnel ()-------");


            personnel.setPictures(pictures);
        }


        personnel.setPicture(file.getBytes()); // file refers to the one acquired in the personnel-page.html form with the name: "imageFile", which is an input with type = "file"

        personnelService.savePersonnel(personnel);

        CustomUser loggedInUser = userService.findUserByUsername(
                getLoggedInUser()
                //"test"
        ).get();
        List<Personnel> usersPersonnelList = loggedInUser.getPersonnelList();


        model.addAttribute("added", "Tillagt: " + personnel.getFirstName());
        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", usersPersonnelList);

        //return "redirect:/personnel";
        return "personnel-page";

    }

    @GetMapping("/allpersonnel")
    public String allPersonnel (Model model) {


        model.addAttribute("personnelList", personnelService.findAll());
        return "personnel-list";
    }

//    @GetMapping("/personnel-info/{id}")
//    public String personnelInfoPage (@PathVariable Long id, Model model) {
//
//        Personnel personnel = personnelService.findPersonnelById(id).get();
//
//        System.out.println("GET----- PersonnelController -- personnelInfoPage ----GET ");
//        System.out.println("personnel.getFirstName: " + personnel.getFirstName());
//        System.out.println("getImages.size: " + personnel.getImages().size());
//        System.out.println("getImages.get0: " + personnel.getImages().get(0));
//        System.out.println("getImages.get1: " + personnel.getImages().get(1));
//        System.out.println("/GET----- PersonnelController -- personnelInfoPage ---- ");
//
//
//        model.addAttribute("personnel", personnel);
//        return "personnel-info-page";
//    }


    @PostMapping("/personnel-info")
    public String personnelInfoPage2 (@ModelAttribute("personnel") Personnel personnel, Model model) {

        System.out.println("POST----- PersonnelController -- personnelInfoPage ----POST ");
//        System.out.println("personnel.getFirstName: " + personnel.getFirstName());
//        System.out.println("getImages.size: " + personnel.getImages().size());
//        System.out.println("getImages.get0: " + personnel.getImages().get(0));
//        System.out.println("getImages.get1: " + personnel.getImages().get(1));
        System.out.println("/POST----- PersonnelController -- personnelInfoPage ---- ");

        //String image = personnel.getImages().get(0);


        model.addAttribute("personnel", personnel);
        return "personnel-info-page";

    }

    @PostMapping("/edit")
    public String editPersonnel (@ModelAttribute Personnel personnel, Model model,
                                 @RequestParam("imageFiles") List<MultipartFile> files,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile file1) throws IOException {

        Personnel personnel1 = personnelService.findPersonnelById(personnel.getId()).get();

        List<String> images = personnel1.getImages();

        // to prevent duplicates
        Set<String> uniqueImages = new HashSet<>(images);

        for (MultipartFile file : files) {

            uniqueImages.add(Base64.getEncoder().encodeToString(file.getBytes()));

        }

        images = new ArrayList<>(uniqueImages);


        personnel1.setFirstName(personnel.getFirstName());
        personnel1.setLastName(personnel.getLastName());
        personnel1.setBranch(personnel.getBranch());
        personnel1.setCountryOfOrigin(personnel.getCountryOfOrigin());

        if (file1 == null) {

            personnel1.setPicture(null);

        } else {

            personnel1.setPicture(file1.getBytes());

        }

        //System.out.println("file1.getBytes(): " + Arrays.toString(file1.getBytes()));
        //personnel1.setPicture(file1.getBytes());





        personnel1.setImages(images);

        personnelService.savePersonnel(personnel1);

        model.addAttribute("personnel", personnel1);

        return "personnel-info-page";

    }



}
