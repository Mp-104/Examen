package com.example.examen.service;

import com.example.examen.model.Personnel;

import java.util.List;

public interface IPersonnelService {
    List<Personnel> findAll();

    void savePersonnel(Personnel personnel);
}
