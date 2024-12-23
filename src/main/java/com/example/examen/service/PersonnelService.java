package com.example.examen.service;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.repository.PersonnelRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

//import java.awt.print.Pageable;
import java.util.*;

import static com.example.examen.placeholder.Placeholder.placeholderImage;
import static com.example.examen.principal.MyPrincipal.getLoggedInUser;

@Service
@Transactional
public class PersonnelService implements IPersonnelService {

    private final IUserService userService;

    private final PersonnelRepository personnelRepository;
    private final CacheManager cacheManager;


    public PersonnelService(IUserService userService, PersonnelRepository personnelRepository
            , CacheManager cacheManager
    ) {
        this.userService = userService;
        this.personnelRepository = personnelRepository;
        this.cacheManager = cacheManager;
    }

    @Override

    @Cacheable(cacheNames = "personnel_all1", key = "'all'")
    public List<Personnel> findAll() {
        return personnelRepository.findAll();
    }

    @Override
    public Page<Personnel> paginateList(List<Personnel> allPersonnel, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Personnel> paginatedList;

        if (allPersonnel.size() < startItem) {
            paginatedList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allPersonnel.size());
            paginatedList = allPersonnel.subList(startItem, toIndex);
        }

        return new PageImpl<>(paginatedList, pageable, allPersonnel.size());
    }

    @Override
    @Cacheable(cacheNames = "personnel_all", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()") // Todo - this seems to have improved performance significantly
    public Page<Personnel> findAllPersonnel (Pageable pageable) {
        return personnelRepository.findAll(pageable);
    }

    @Override
    //@Cacheable(cacheNames = "personnelCache2")
    //@Cacheable(cacheNames = "personnelCache")
    @Cacheable(cacheNames = "personnel_by_country", key = "#country + '_' + #pageNumber + '_' + #pageSize")
    public Page<Personnel> findPersonnelByCountryAllegiance (String country, int pageNumber, int pageSize, String sortBy) throws InstantiationException, IllegalAccessException {


        /*
        List<Personnel> allPersonnel = (List<Personnel>) cacheManager.getCache("personnel_all").get("all").get();

        if (allPersonnel != null) {
            List<Personnel> filteredPersonnelList = allPersonnel.stream()
                    .filter(personnel -> country.equals(personnel.getCountryAllegiance()))
                    .collect(Collectors.toList());

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredPersonnelList.size());

            return new PageImpl<>(filteredPersonnelList.subList(start, end), pageable, filteredPersonnelList.size());
        }

         */

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        return personnelRepository.findPersonnelByCountryAllegiance(country, pageable);
    }

    @Override
    //@org.springframework.transaction.annotation.Transactional
    //@Cacheable(cacheNames = "personnelCache")
    @Cacheable(cacheNames = "personnel_by_id", key = "#id")
    public Optional<Personnel> findPersonnelById(Long id) {
        return personnelRepository.findById(id);
    }

    @Override
    //@CacheEvict(cacheNames = {"personnelCache", "personnelCache2"}, allEntries = true)
    //@CacheEvict(cacheNames = {"personnel_by_id"}, key = "#id")
    @CacheEvict(cacheNames = {"personnel_all"}, allEntries = true)
    //@CacheEvict(cacheNames = {"personnel_by_country"}, allEntries = true)
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
    //@CachePut(cacheNames = "personnelCache2", key = "'personnel_' + #personnel.id")
    //@CacheEvict(cacheNames = {"personnelCache", "personnelCache2"}, allEntries = true)
    //@CachePut(cacheNames = {"personnel_by_id"}, key = "#personnel.id")
    //@CachePut(cacheNames = {"personnel_all"}, key = "'all'")
    //@CachePut(cacheNames = {"personnel_all"}, key = "'page_0_10_id:ASC'")
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

        Cache personnelByIdCache = cacheManager.getCache("personnel_by_id");
        if (personnelByIdCache != null) {
            personnelByIdCache.put(personnel.getId(), personnel);
        }

        Cache personnelAllCache = cacheManager.getCache("personnel_all1");
        if (personnelAllCache != null) {
            personnelAllCache.put("all", personnelRepository.findAll());
        }

        Cache personnelPageCache = cacheManager.getCache("personnel_all");
        if (personnelPageCache != null) {
            personnelPageCache.put("page_0_3_id:ASC", personnelRepository.findAll(PageRequest.of(0, 3, Sort.by("firstName").ascending())));
        }
    }
}
