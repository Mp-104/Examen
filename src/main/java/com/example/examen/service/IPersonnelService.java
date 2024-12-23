package com.example.examen.service;

import com.example.examen.model.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPersonnelService {
    List<Personnel> findAll();

    Page<Personnel> findAllPersonnel (Pageable pageable);

    Page<Personnel> findPersonnelByCountryAllegiance(String country, int pageNumber, int pageSize, String sortBy) throws InstantiationException, IllegalAccessException;

    Optional<Personnel> findPersonnelById(Long id);

    String deletePersonnelById(Long id);

    void savePersonnel(Personnel personnel);
}
