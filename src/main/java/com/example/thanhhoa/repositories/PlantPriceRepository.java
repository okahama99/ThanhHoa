package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.PlantPrice;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlantPriceRepository extends JpaRepository<PlantPrice, String> {
    Optional<PlantPrice> findById(String plantPriceID);

    PlantPrice findFirstByPlant_IdAndStatusOrderByApplyDateDesc(String plantID, Status status);

    PlantPrice findFirstByOrderByIdDesc();

    PlantPrice findByPriceAndApplyDateAndStatus(Double price, LocalDateTime applyDate, Status status);

    List<PlantPrice> findByPlant_IdAndStatus(String plantID, Status status);

    PlantPrice findByPlant_IdAndPriceBetweenAndStatus(String plantID, Double fromPrice, Double toPrice, Status status);
}
