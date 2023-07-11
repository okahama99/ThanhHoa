package com.example.thanhhoa.repositories;

import com.example.thanhhoa.entities.StorePlant;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorePlantRepository extends JpaRepository<StorePlant, String> {
    StorePlant findByPlantIdAndStoreIdAndPlant_Status(String plantID, String storeID, Status status);

    List<StorePlant> findByStore_IdAndPlant_StatusAndStore_Status(String storeID, Status plantStatus, Status storeStatus);

    List<StorePlant> findByStore_Id(String storeID);

    StorePlant findFirstByOrderByIdDesc();
}
