package com.example.examen.repository;

import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

//import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    Optional<Personnel> findPersonnelById (Long id);
    //Page<Personnel> findAllPersonnel (Pageable pageable);

    @Override
    Page<Personnel> findAll (Pageable pageable);

    Page<Personnel> findPersonnelByCountryAllegiance (String country, Pageable page);
}
