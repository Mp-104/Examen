package com.example.examen.service;

import com.example.examen.model.Personnel;

import java.util.List;
import java.util.Optional;

public interface IPersonnelService {
    List<Personnel> findAll();

    Optional<Personnel> findPersonnelById(Long id);

    void savePersonnel(Personnel personnel);
}
