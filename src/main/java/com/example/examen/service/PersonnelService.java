package com.example.examen.service;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.repository.PersonnelRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

//import java.awt.print.Pageable;
import java.util.*;

import static com.example.examen.placeholder.Placeholder.placeholderImage;
import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

@Service
@Transactional
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
    @Cacheable(cacheNames = "personnelCache") // Todo - this seems to have improved performance significantly
    public Page<Personnel> findAllPersonnel (Pageable pageable) {
        return personnelRepository.findAll(pageable);
    }

    @Override
    @Cacheable(cacheNames = "personnelCache2")
    public Page<Personnel> findPersonnelByCountryAllegiance (String country, int pageNumber, int pageSize, String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        return personnelRepository.findPersonnelByCountryAllegiance(country, pageable);
    }

    @Override
    //@org.springframework.transaction.annotation.Transactional
    public Optional<Personnel> findPersonnelById(Long id) {
        return personnelRepository.findById(id);
    }

    @Override
    public String deletePersonnelById(Long id) {

        System.out.println("PersonnelService, deletePersonnelById deleting: " + id);


        if (personnelRepository.findById(id).isPresent()) {


            System.out.println("Removing Personnel with id: " + id);
            Personnel personnel = personnelRepository.findById(id).get();

            CustomUser customUser = personnel.getCustomUser();
            customUser.getPersonnelList().remove(personnel);
            personnelRepository.deleteById(id);


            return "Personnel med id: " + id + " borttagen";
        } else {

            return "Personnel med id: " + id + " kunde inte hittas";
        }
    }

    @Override
    public void savePersonnel(Personnel personnel) {

        List<String> imageList = new ArrayList<>();
//        System.out.println("personnel.getPictures(): " + personnel.getPictures());
//        System.out.println("personnel.getImages(): " + personnel.getImages());




        //TODO fix so it does not saves empty values - fixed in Controller
        if (personnel.getImages() == null && personnel.getPictures() != null
                //|| !Arrays.toString(personnel.getPictures().get(0)).equals("[]")
                //|| !Objects.equals(personnel.getImages().get(0), "[]")
        ) {
            //System.out.println("personnel.getPictures(): " + personnel.getPictures());
//            System.out.println("savePersonnel" );
//            System.out.println("personnel.getPictures().sizer(): " + personnel.getPictures().size());
//            System.out.println("getPictures.get(0): " + personnel.getPictures().get(0));
            for (byte[] picture : personnel.getPictures()) {
                String base64 = Base64.getEncoder().encodeToString(picture);
                imageList.add(base64);
            }
            //System.out.println("imageList.size: " + imageList.size());
            if (imageList.size() > 0) {
                System.out.println("it saved");
                personnel.setImages(imageList);
            }


        }






        // ----------------------------------------------------------------------------------------//
        //System.out.println("PersonnelService: personnel.getPicture(): " + Arrays.toString(personnel.getPicture()));

        if (personnel.getImage() == null) {
            //System.out.println("outside if: Arrays.toString(personnel.getPicture()): " + Arrays.toString(personnel.getPicture()));

            if (Arrays.toString(personnel.getPicture()).equals("[]")) {
                //System.out.println("inside if: Arrays.toString(personnel.getPicture()): " + Arrays.toString(personnel.getPicture()));

                personnel.setImage(placeholderImage());

            } else {

                String base64 = Base64.getEncoder().encodeToString(personnel.getPicture());

                personnel.setImage(base64);
            }

        } else if (Objects.equals(personnel.getImage(), placeholderImage())
                && !Arrays.toString(personnel.getPicture()).equals("[]") && personnel.getPicture() != null
        ) {

            String base64 = Base64.getEncoder().encodeToString(personnel.getPicture());

            personnel.setImage(base64);

        } else if (personnel.getPicture() != null  && !Arrays.toString(personnel.getPicture()).equals("[]")) {

            String base64 = Base64.getEncoder().encodeToString(personnel.getPicture());

            personnel.setImage(base64);

        }

        if (personnel.getCustomUser() == null) {
            CustomUser loggedInUser = userService.findUserByUsername(getLoggedInUser()).get();

            personnel.setCustomUser(loggedInUser);
        }




        //Todo - see if this can be made more efficient
        if (personnel.getImages() != null) {


            if (Objects.equals(personnel.getImages().toString(), "[]") || personnel.getImages().get(0).isBlank()) {


                if (personnel.getImages().size() > 1) {

                    personnel.getImages().remove(0);

                    personnel.setImages(personnel.getImages());

                } else {
                    personnel.setImages(null);
                }

            }

        }

//        System.out.println("personnel.getPictures(): what is saved: " + personnel.getPictures());
//        System.out.println("personnel.getImages(): what is saved: " + personnel.getImages());
        personnelRepository.save(personnel);
    }
}
