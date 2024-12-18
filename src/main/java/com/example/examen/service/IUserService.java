package com.example.examen.service;

import com.example.examen.dto.PasswordDT0;
import com.example.examen.dto.UserDTO;
import com.example.examen.model.CustomUser;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<CustomUser> findUserByUsername(String username);

    String saveUser(UserDTO user);

    String changePassword(PasswordDT0 password);

    void disableUser();
}
