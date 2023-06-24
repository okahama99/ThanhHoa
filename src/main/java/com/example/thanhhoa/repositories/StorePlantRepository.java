package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, String> {
    StorePlant findByPlantIdAndStoreId(String plantID, String storeID);

    List<StorePlant> findByPlantId(String plantID);

    StorePlant findFirstByOrderByIdDesc();
}
