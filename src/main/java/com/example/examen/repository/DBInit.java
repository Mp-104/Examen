package com.example.examen.repository;

import com.example.examen.authorities.UserRole;
import com.example.examen.dto.UserDTO;
import com.example.examen.model.CustomUser;
import com.example.examen.service.IUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DBInit {

    private final IUserService userService;



    public DBInit(IUserService userService) {
        this.userService = userService;
    }

    /*
    @PostConstruct
    public void createUser () {


        UserDTO user1 = new UserDTO("test", "test", UserRole.ADMIN);
        UserDTO user2 = new UserDTO("test2", "test", UserRole.USER);
        UserDTO user3 = new UserDTO("test3", "test", UserRole.GUEST);


        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

    }

     */
}
