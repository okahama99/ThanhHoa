package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategory, String> {
    List<PlantCategory> findAllByPlant_IdAndStatus(String plantID, Status status);

    List<PlantCategory> findByPlantAndStatus(Plant plant, Status status);

    List<PlantCategory> findByCategory_IdAndStatus(String categoryID, Status status);

    List<PlantCategory> findByCategory_IdAndPlant_NameContainingAndStatus(String categoryID, String plantName, Status status);

    PlantCategory findFirstByOrderByIdDesc();

    PlantCategory findByIdAndStatus(String plantCategoryID, Status status);
}
