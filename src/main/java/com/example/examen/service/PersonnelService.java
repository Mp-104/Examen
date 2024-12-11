package com.example.examen.service;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.principal.MyPrincipal;
import com.example.examen.repository.PersonnelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

@Service
public class PersonnelService implements IPersonnelService {

    private final IUserService userService;

    private final PersonnelRepository personnelRepository;

    public PersonnelService(IUserService userService, PersonnelRepository personnelRepository) {
        this.userService = userService;
        this.personnelRepository = personnelRepository;
    }

    @Override
    public List<Personnel> findAll() {
        return personnelRepository.findAll();
    }

    @Override
    public void savePersonnel(Personnel personnel) {

        List<String> imageList = new ArrayList<>();

        for (byte[] picture : personnel.getPictures()) {
            String base64 = Base64.getEncoder().encodeToString(picture);
            imageList.add(base64);
        }

        personnel.setImages(imageList);



        // ----------------------------------------------------------------------------------------//
        String base64 = Base64.getEncoder().encodeToString(personnel.getPicture());

        personnel.setImage(base64);


        CustomUser loggedInUser = userService.findUserByUsername(getLoggedInUser()).get();

        personnel.setCustomUser(loggedInUser);

        personnelRepository.save(personnel);
    }
}