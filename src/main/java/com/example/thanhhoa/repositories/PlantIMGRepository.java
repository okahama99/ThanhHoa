package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantIMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantIMGRepository extends JpaRepository<PlantIMG, Long> {
}
