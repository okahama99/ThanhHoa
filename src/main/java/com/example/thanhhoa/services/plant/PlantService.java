package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.entities.Plant;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantService {
    List<ShowPlantModel> getAllPlant(Pageable paging);

    List<ShowPlantModel> getAllPlantWithInactive(Pageable paging);

    List<ShowPlantModel> getPlantV2(String storeID, Pageable pageable);

    ShowPlantModel getPlantByID(String plantID);

    String createPlant(CreatePlantModel createPlantModel) throws Exception;

    String updatePlant(UpdatePlantModel updatePlantModel) throws Exception;

    String activatePlant(String plantID) throws Exception;

    String activatePlantCategory(String plantCategoryID) throws Exception;

    String deletePlant(String plantID);

    String deletePlantIMG(String plantIMG);

    Plant checkDuplicate(String plantName);

    List<String> getPlantIMGByPlantID(String plantID);

    List<ShowPlantModel> getPlantByCategory(String categoryID, Pageable paging);

    List<ShowPlantModel> getPlantByName(String name, Pageable paging);

    List<ShowPlantModel> getPlantByPriceMin(Double minPrice, Pageable paging);

    List<ShowPlantModel> getPlantByPriceMax(Double maxPrice, Pageable paging);

    List<ShowPlantModel> getPlantByPriceInRange(Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndName(String categoryID, String name, Pageable paging);

    List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndPrice(String categoryID, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(String categoryID, String name, Double fromPrice, Double toPrice, Pageable paging);
}
