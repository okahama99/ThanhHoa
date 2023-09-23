package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantStatusIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantStatusIMGRepository extends JpaRepository<PlantStatusIMG, String> {
    PlantStatusIMG findFirstByOrderByIdDesc();
}
