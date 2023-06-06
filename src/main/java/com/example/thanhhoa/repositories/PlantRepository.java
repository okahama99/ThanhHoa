package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Plant findByName(String plantName);

    List<Plant> findByNameLikeAndStatus(String name, Status status);

    List<Plant> findByPriceBetweenAndStatus(Double fromPrice, Double toPrice, Status status);

    List<Plant> findByPriceBetweenAndNameAndStatus(Double fromPrice, Double toPrice, String name, Status status);
}
