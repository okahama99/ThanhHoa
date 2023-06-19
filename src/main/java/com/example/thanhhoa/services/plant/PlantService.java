package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.entities.Plant;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlantService {
    List<ShowPlantModel> getAllPlant(Pageable paging);

    ShowPlantModel getPlantByID(String plantID);

    String createPlant(CreatePlantModel createPlantModel) throws Exception;

    String updatePlant(UpdatePlantModel updatePlantModel, List<MultipartFile> files) throws Exception;

    Boolean deletePlant(String plantID);

    Plant checkDuplicate(String plantName);

    List<ShowPlantModel> getPlantByCategory(String categoryID, Pageable paging);

    List<ShowPlantModel> getPlantByName(String name, Pageable paging);

    List<ShowPlantModel> getNameByPriceMin(Double minPrice, Pageable paging);

    List<ShowPlantModel> getNameByPriceMax(Double maxPrice, Pageable paging);

    List<ShowPlantModel> getNameByPriceInRange(Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndName(String categoryID, String name, Pageable paging);

    List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndPrice(String categoryID, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(String categoryID, String name, Double fromPrice, Double toPrice, Pageable paging);
}
