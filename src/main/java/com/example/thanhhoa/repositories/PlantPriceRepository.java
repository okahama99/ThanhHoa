package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PlantPriceRepository extends JpaRepository<PlantPrice, String> {
    Optional<PlantPrice> findById(String plantPriceID);

    PlantPrice findFirstByPlant_IdOrderByApplyDateDesc(String plantID);

    PlantPrice findFirstByOrderByIdDesc();

    PlantPrice findByPriceAndApplyDate(Double price, LocalDateTime applyDate);
}
