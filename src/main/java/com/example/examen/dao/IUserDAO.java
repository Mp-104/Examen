package com.example.examen.dao;

import com.example.examen.model.CustomUser;

import java.util.Optional;

public interface IUserDAO {

    Optional<CustomUser> findByUsername(String username);

    void save(CustomUser customUser);
}
