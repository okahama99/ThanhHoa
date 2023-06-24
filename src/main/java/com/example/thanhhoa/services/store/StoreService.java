package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {
    String addStorePlant(AddStorePlantModel addStorePlantModel) throws Exception;

    List<ShowStoreModel> getAllStore();

    List<ShowPlantModel> getStorePlantByStoreID(String storeID, Pageable pageable);

    String addStoreEmployee(AddStoreEmployeeModel addStoreEmployeeModel);
}
