package com.example.thanhhoa.services.plant;

import com.example.thanhhoa.dtos.PlantModels.CreatePlantModel;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantModel;
import com.example.thanhhoa.dtos.PlantModels.UpdatePlantModel;
import com.example.thanhhoa.entities.Category;
import com.example.thanhhoa.entities.Plant;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlantService {
    List<ShowPlantModel> getAllPlant(Pageable paging);

    Boolean createPlant(CreatePlantModel createPlantModel, MultipartFile[] files) throws Exception;

    Boolean updatePlant(UpdatePlantModel updatePlantModel, MultipartFile[] files) throws Exception;

    Boolean deletePlant(Long plantID) throws IOException;

    Plant checkDuplicate(String plantName);

    List<ShowPlantModel> getPlantByCategory(List<Long> categoryIDList, Pageable paging);

    List<ShowPlantModel> getPlantByName(String name, Pageable paging);

    List<ShowPlantModel> getNameByPrice(Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndName(List<Long> categoryIDList, String name, Pageable paging);

    List<ShowPlantModel> getPlantByNameAndPrice(String name, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndPrice(List<Long> categoryIDList, Double fromPrice, Double toPrice, Pageable paging);

    List<ShowPlantModel> getPlantByCategoryAndNameAndPrice(List<Long> categoryIDList, String name, Double fromPrice, Double toPrice, Pageable paging);
}
