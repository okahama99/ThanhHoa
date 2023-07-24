package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, String> {
    StorePlant findByPlantIdAndStoreIdAndPlant_Status(String plantID, String storeID, Status status);

    StorePlant findByPlant_IdAndPlant_Status(String plantID, Status status);

    StorePlant findFirstByOrderByIdDesc();
}
