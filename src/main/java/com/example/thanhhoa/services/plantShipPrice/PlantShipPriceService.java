package com.example.thanhhoa.services.plantShipPrice;

import com.example.thanhhoa.dtos.PlantShipPriceModels.CreatePlantShipPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantShipPriceService {
    List<ShowPlantShipPriceModel> getAllPlantShipPrice(Pageable pageable);

    String createPSP(CreatePlantShipPriceModel createPlantShipPriceModel);
    String deletePSP(String plantShipPriceID);
}
