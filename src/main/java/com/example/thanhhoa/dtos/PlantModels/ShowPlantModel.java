package com.example.thanhhoa.dtos.PlantModels;

import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowPlantModel {
    private String plantID;
    private String name;
    private Double height;
    private Boolean withPot;
    private Double totalPage;

    private ShowPlantShipPriceModel showPlantShipPriceModel;
    private ShowPlantPriceModel showPlantPriceModel;

    private List<ShowPlantCategory> plantCategoryList;
}
