package com.example.thanhhoa.services.store;

import com.example.thanhhoa.dtos.PlantModels.AddStorePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.StoreModels.AddStoreEmployeeModel;
import com.example.thanhhoa.dtos.StoreModels.CreateStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowDistrictModel;
import com.example.thanhhoa.dtos.StoreModels.ShowProvinceModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStorePlantRecordModel;
import com.example.thanhhoa.dtos.StoreModels.UpdateStoreModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {

    String createStore(CreateStoreModel createStoreModel);

    String updateStore(UpdateStoreModel updateStoremodel);

    String deleteStore(String storeID);

    ShowStoreModel getByID(String storeID);

    String addStorePlant(AddStorePlantModel addStorePlantModel) throws Exception;

    List<ShowStoreModel> getAllStore();

    List<ShowPlantModel> getStorePlantByStoreID(String storeID, Pageable pageable);

    String addStoreEmployee(AddStoreEmployeeModel addStoreEmployeeModel);

    List<ShowDistrictModel> getDistrictByProvinceID(String provinceID);

    List<ShowProvinceModel> getAllProvince();

    List<ShowStorePlantRecordModel> getStorePlantRecordByStorePlantID(String storePlantID);


}
