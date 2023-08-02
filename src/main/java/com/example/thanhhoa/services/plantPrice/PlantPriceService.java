package com.example.thanhhoa.services.plantPrice;

import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantPriceService {

    List<ShowPlantPriceModel> getAll(Pageable pageable);

    List<ShowPlantPriceModel> getAllByPlantID(String plantID, Pageable pageable);

    ShowPlantPriceModel getByID(String plantPriceID);
}
