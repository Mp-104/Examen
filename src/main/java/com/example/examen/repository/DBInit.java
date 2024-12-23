package com.example.examen.repository;

import com.example.examen.model.Personnel;
import com.example.examen.service.IPersonnelService;
import com.example.examen.service.IUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DBInit {

    private final IUserService userService;
    private final CacheManager cacheManager;

    private final IPersonnelService personnelService;


    public DBInit(IUserService userService, CacheManager cacheManager, IPersonnelService personnelService) {
        this.userService = userService;
        this.cacheManager = cacheManager;
        this.personnelService = personnelService;
    }

    @PostConstruct
    public void preWarm () throws InstantiationException, IllegalAccessException {

        preloadCache();
    }

    @Async
    public void preloadCache () throws InstantiationException, IllegalAccessException {
        System.out.println("warming up cache fetching all personnel..");
        //Pageable pageable = PageRequest.of(0, 3, Sort.by("firstName"));
        String key = "USA" + "_" + 0 + "_" + 3;

        List<Personnel> personnelList = personnelService.findAll();
        //Page<Personnel> personnelPage = personnelService.findAllPersonnel(pageable);
        //Page<Personnel> personnelCountryPage = personnelService.findPersonnelByCountryAllegiance("USA", 0, 3, "firstName");

        cacheManager.getCache("personnel_all1").put("all", personnelList);
        //cacheManager.getCache("personnel_all").put("all", personnelPage);
        //cacheManager.getCache("personnel_by_country").put(key, personnelCountryPage);

        int pagesToPreload = 5;
        int pageSize = 3;
        Sort sort = Sort.by("firstName");

        /*
        for (int pageNumber = 0; pageNumber < pagesToPreload; pageNumber ++) {
            Pageable pageable1 = PageRequest.of(pageNumber, pageSize, sort);
            Page<Personnel> personnelPage1 = personnelService.findAllPersonnel(pageable1);

            String key1 = "page_" + pageNumber + "_" + pageSize + "_" + sort.toString();

            cacheManager.getCache("personnel_all").put(key1, personnelPage1);
        }

         */

        System.out.println("done fetching all personnel");
    }

    /*
    @PostConstruct
    public void createUser () {


        UserDTO user1 = new UserDTO("test", "test", "test", "test", UserRole.ADMIN);
        UserDTO user2 = new UserDTO("test2", "test", "test2", "test", UserRole.USER);
        UserDTO user3 = new UserDTO("test3", "test", "test3", "test", UserRole.GUEST);


        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

    }

     */


}



