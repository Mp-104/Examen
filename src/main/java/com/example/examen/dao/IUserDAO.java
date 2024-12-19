package com.example.examen.dao;

import com.example.examen.model.CustomUser;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    Optional<CustomUser> findByUsername(String username);

    List<CustomUser> getAllUsers();

    void save(CustomUser customUser);

    Optional<CustomUser> findUserById(Long id);
}
