package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Plant findByName(String plantName);
}
