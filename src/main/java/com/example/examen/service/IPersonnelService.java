package com.example.examen.service;

import com.example.examen.model.Personnel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IPersonnelService {
    List<Personnel> findAll();

    Page<Personnel> findPersonnelByCountryAllegianceUSA(int pageNumber, int pageSize, String sortBy);

    Optional<Personnel> findPersonnelById(Long id);

    String deletePersonnelById(Long id);

    void savePersonnel(Personnel personnel);
}
