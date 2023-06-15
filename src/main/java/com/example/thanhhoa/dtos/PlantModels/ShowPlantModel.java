package com.example.thanhhoa.dtos.PlantModels;

import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowPlantModel {
    private Long plantID;
    private String name;
    private Double height;
    private Double price;
    private Boolean withPot;
    private Double totalPage;

    private ShowPlantShipPriceModel showPlantShipPriceModel;

    private List<ShowPlantCategory> plantCategoryList;
}
