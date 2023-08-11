package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, String> {
    StorePlant findByPlantIdAndStoreIdAndPlant_Status(String plantID, String storeID, Status status);

    StorePlant findByPlantIdAndStoreId(String plantID, String storeID);

    List<StorePlant> findByPlantIdAndQuantityGreaterThanEqual(String plantID, Integer quantity);

    StorePlant findByPlant_IdAndPlant_Status(String plantID, Status status);

    StorePlant findFirstByOrderByIdDesc();

    @Query(value = "SELECT SUM(sp.quantity) FROM store_plant sp WHERE sp.plant_id = ?", nativeQuery = true)
    Integer sumQuantity(String plantID);
}
