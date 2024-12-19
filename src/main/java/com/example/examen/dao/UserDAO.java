package com.example.examen.dao;

import com.example.examen.model.CustomUser;
import com.example.examen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO implements IUserDAO{

    private final UserRepository userRepository;

    @Autowired
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<CustomUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<CustomUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void save (CustomUser customUser) {
        userRepository.save(customUser);
    }

    @Override
    public Optional<CustomUser> findUserById(Long id) {
        return userRepository.findById(id);
    }


}
