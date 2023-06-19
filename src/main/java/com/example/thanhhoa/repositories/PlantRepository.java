package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Plant findByName(String plantName);

    Optional<Plant> findById(String plantID);

    Plant findFirstByStatusOrderByIdDesc(Status status);

    List<Plant> findByPlantPrice_PriceBetweenAndStatus(Double fromPrice, Double toPrice, Status status);

    List<Plant> findByPlantPrice_PriceBetweenAndNameAndStatus(Double fromPrice, Double toPrice, String name, Status status);
}
