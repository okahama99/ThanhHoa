package com.example.thanhhoa.dtos.PlantModels;

import com.example.thanhhoa.entities.PlantCategory;
import com.example.thanhhoa.entities.PlantShipPrice;
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

    private Long shipPriceID;
    private String potSize;
    private Double pricePerPlant;

    private List<ShowPlantCategory> plantCategoryList;
}
