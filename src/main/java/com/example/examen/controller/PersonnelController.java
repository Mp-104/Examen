package com.example.examen.controller;

import com.example.examen.countries.NatoCountries;
import com.example.examen.dto.PersonnelDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.service.IPersonnelService;
import com.example.examen.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.example.examen.countries.NatoCountries.getAllNatoCountries;
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
    @Transactional
    public String getPersonalPage (Model model) {

        /*
        CustomUser loggedInUser = userService.findUserByUsername(
                getLoggedInUser()
                //"test"
        ).get();
        List<Personnel> usersPersonnelList = loggedInUser.getPersonnelList();

         */

//        Personnel firstPersonnel = usersPersonnelList.get(0);
//
//        model.addAttribute("firstPersonnel", firstPersonnel);

        List<Personnel> allPersonnel = personnelService.findAll();
        List<Personnel> loggedInUsersList = new ArrayList<>();

        for (Personnel personnel : allPersonnel) {
            if (Objects.equals(personnel.getCustomUser().getUsername(), getLoggedInUser())) {
                loggedInUsersList.add(personnel);
            }
        }


        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", loggedInUsersList);

        model.addAttribute("countries", getAllNatoCountries());

        return "personnel-page";
    }

    @PostMapping("/personnel")
    public String addPersonnel (@ModelAttribute("personnel") Personnel personnel,
                                Model model,
                                @RequestParam("imageFiles") List<MultipartFile> files,
                                @RequestParam("imageFile") MultipartFile file // Make sure the template form has: enctype="multipart/form-data", which allows the form to be sent as a Multipart file with a Personnel object and image file
    ) throws IOException {

        // Todo - clean up and move to service class
        CustomUser loggedInUser = userService.findUserByUsername(
                getLoggedInUser()
                //"test"
        ).get();

        List<Personnel> usersPersonnelList = loggedInUser.getPersonnelList();

        // Checks if file or one of the files is over 1500000 bytes (1,5 MB) in size
        if (file.getSize() > 1500000 || files.stream().anyMatch( multipartFile -> multipartFile.getSize() > 1500000) ) {

            model.addAttribute("personnel", new Personnel());
            //model.addAttribute("personnelList", usersPersonnelList);

            model.addAttribute("countries", getAllNatoCountries());

            model.addAttribute("error", "fil för stor, får inte vara större än 1,5 MB");

            //return "redirect:/personnel";
            return "personnel-page";
        }

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

        usersPersonnelList = userService.findUserByUsername(getLoggedInUser()).get().getPersonnelList();

        model.addAttribute("added", "Tillagt: " + personnel.getFirstName());
        model.addAttribute("personnel", new Personnel());
        model.addAttribute("personnelList", usersPersonnelList);

        model.addAttribute("countries", getAllNatoCountries());

        //return "redirect:/personnel";
        return "personnel-page";

    }

    @GetMapping("/allpersonnel")
    public String allPersonnel (Model model) {


        model.addAttribute("personnelList", personnelService.findAll());
        return "personnel-list";
    }



    @GetMapping("/personnel-info")
    //@Transactional
    public String personnelInfoPage (@ModelAttribute("personnel") Personnel personnel, Model model) {

        Personnel foundPersonnel = personnelService.findPersonnelById(personnel.getId()).get();
        System.out.println("POST----- PersonnelController -- personnelInfoPage ----POST ");
//        System.out.println("personnel.getFirstName: " + personnel.getFirstName());
//        System.out.println("getImages.size: " + personnel.getImages().size());
//        System.out.println("getImages.get0: " + personnel.getImages().get(0));
//        System.out.println("getImages.get1: " + personnel.getImages().get(1));
        System.out.println("/POST----- PersonnelController -- personnelInfoPage ---- ");

        foundPersonnel.getCustomUser().getUsername();
        //String image = personnel.getImages().get(0);


        //model.addAttribute("personnel", personnel);
        model.addAttribute("personnel", foundPersonnel);
        model.addAttribute("countries", getAllNatoCountries());
        return "personnel-info-page";

    }

    @PostMapping("/personnel-info")
    //@Transactional
    public String personnelInfoPage2 (@ModelAttribute("personnel") Personnel personnel, Model model) {

        Personnel foundPersonnel = personnelService.findPersonnelById(personnel.getId()).get();
        System.out.println("POST----- PersonnelController -- personnelInfoPage ----POST ");
//        System.out.println("personnel.getFirstName: " + personnel.getFirstName());
//        System.out.println("getImages.size: " + personnel.getImages().size());
//        System.out.println("getImages.get0: " + personnel.getImages().get(0));
//        System.out.println("getImages.get1: " + personnel.getImages().get(1));
        System.out.println("/POST----- PersonnelController -- personnelInfoPage ---- ");

        foundPersonnel.getCustomUser().getUsername();
        //String image = personnel.getImages().get(0);


        //model.addAttribute("personnel", personnel);
        model.addAttribute("personnel", foundPersonnel);
        model.addAttribute("countries", getAllNatoCountries());
        return "personnel-info-page2";

    }

    @PostMapping("/edit")
    public String editPersonnel (@ModelAttribute Personnel personnel, Model model,
                                 @RequestParam("imageFiles") List<MultipartFile> files,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile multipartFile) throws IOException {


        Personnel personnelToEdit = personnelService.findPersonnelById(personnel.getId()).get();

        if ((files != null && multipartFile !=null) && multipartFile != null) {

            if(multipartFile.getSize() > 1500000 || files.stream().anyMatch(file -> file.getSize() > 1500000)) {

                model.addAttribute("error", "fil för stor, får inte vara större än 1,5 MB");
                model.addAttribute("personnel", personnelToEdit);
                model.addAttribute("countries", getAllNatoCountries());
                return "personnel-info-page";
            }
        }



        //System.out.println("PersonnelController, editPersonnel: multipartFile.getSize(): " + multipartFile.getSize() + " bytes");
        List<String> images = personnelToEdit.getImages();

        // to prevent duplicates
        if (images != null) {
            Set<String> uniqueImages = new HashSet<>(images);

            for (MultipartFile file : files) {

                uniqueImages.add(Base64.getEncoder().encodeToString(file.getBytes()));

            }

            images = new ArrayList<>(uniqueImages);
        }



        personnelToEdit.setFirstName(personnel.getFirstName());
        personnelToEdit.setLastName(personnel.getLastName());
        personnelToEdit.setBranch(personnel.getBranch());
        personnelToEdit.setCountryAllegiance(personnel.getCountryAllegiance());
        personnelToEdit.setRank(personnel.getRank());
        personnelToEdit.setHomeAddress(personnel.getHomeAddress());
        personnelToEdit.setDescription(personnel.getDescription());

        if (multipartFile == null) {

            personnelToEdit.setPicture(null);

        } else {

            personnelToEdit.setPicture(multipartFile.getBytes());

        }

        //System.out.println("file1.getBytes(): " + Arrays.toString(file1.getBytes()));
        //personnel1.setPicture(file1.getBytes());


        personnelToEdit.setImages(images);

        personnelService.savePersonnel(personnelToEdit);

        model.addAttribute("personnel", personnelToEdit);
        model.addAttribute("countries", getAllNatoCountries());

        return "personnel-info-page";

    }

    @PostMapping("/edit2")
    public String editPersonnel2 (@ModelAttribute Personnel personnel, Model model,
                                 @RequestParam("imageFiles") List<MultipartFile> files,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile multipartFile) throws IOException {


        Personnel personnelToEdit = personnelService.findPersonnelById(personnel.getId()).get();

        if ((files != null && multipartFile !=null) && multipartFile != null) {

            if(multipartFile.getSize() > 1500000 || files.stream().anyMatch(file -> file.getSize() > 1500000)) {

                model.addAttribute("error", "fil för stor, får inte vara större än 1,5 MB");
                model.addAttribute("personnel", personnelToEdit);
                model.addAttribute("countries", getAllNatoCountries());
                return "personnel-info-page2";
            }
        }



        //System.out.println("PersonnelController, editPersonnel: multipartFile.getSize(): " + multipartFile.getSize() + " bytes");
        List<String> images = personnelToEdit.getImages();

        // to prevent duplicates
        if (images != null) {

            Set<String> uniqueImages = new HashSet<>(images);

            for (MultipartFile file : files) {

                uniqueImages.add(Base64.getEncoder().encodeToString(file.getBytes()));

            }

            images = new ArrayList<>(uniqueImages);
        }





        personnelToEdit.setFirstName(personnel.getFirstName());
        personnelToEdit.setLastName(personnel.getLastName());
        personnelToEdit.setBranch(personnel.getBranch());
        personnelToEdit.setCountryAllegiance(personnel.getCountryAllegiance());
        personnelToEdit.setRank(personnel.getRank());
        personnelToEdit.setHomeAddress(personnel.getHomeAddress());
        personnelToEdit.setDescription(personnel.getDescription());

        if (multipartFile == null) {

            personnelToEdit.setPicture(null);

        } else {

            personnelToEdit.setPicture(multipartFile.getBytes());

        }

        //System.out.println("file1.getBytes(): " + Arrays.toString(file1.getBytes()));
        //personnel1.setPicture(file1.getBytes());


        personnelToEdit.setImages(images);

        personnelService.savePersonnel(personnelToEdit);

        model.addAttribute("personnel", personnelToEdit);
        model.addAttribute("countries", getAllNatoCountries());

        return "personnel-info-page2";

    }



//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public String handleFileSizeException (MaxUploadSizeExceededException exception, Model model) {
//        model.addAttribute("error", "fil för stor");
//        return "personnel-info-page";
//    }

}



//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public String handleFileSizeException (MaxUploadSizeExceededException exception, Model model) {
//        model.addAttribute("error", "fil för stor");
//        return "personnel-info-page";
//    }


