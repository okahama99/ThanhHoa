package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategory, String> {
    List<PlantCategory> findAllByPlant_Id(String plantID);

    List<PlantCategory> findByPlant(Plant plant);

    List<PlantCategory> findByCategory_Id(String categoryID);

    List<PlantCategory> findByCategory_IdAndPlant_NameContaining(String categoryID, String plantName);

    PlantCategory findFirstByOrderByIdDesc();
}
