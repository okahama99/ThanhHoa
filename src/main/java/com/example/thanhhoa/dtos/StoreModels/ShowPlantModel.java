package com.example.thanhhoa.dtos.StoreModels;

import com.example.thanhhoa.dtos.PlantModels.ShowPlantCategory;
import com.example.thanhhoa.dtos.PlantModels.ShowPlantIMGModel;
import com.example.thanhhoa.dtos.PlantPriceModels.ShowPlantPriceModel;
import com.example.thanhhoa.dtos.PlantShipPriceModels.ShowPlantShipPriceModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowPlantModel {
    private String plantID;
    private String name;
    private String height;
    private String unit;
    private Boolean withPot;
    private Double totalPage;
    private String description;
    private String careNote;
    private Status status;

    private ShowPlantShipPriceModel showPlantShipPriceModel;
    private ShowPlantPriceModel showPlantPriceModel;
    private List<ShowPlantIMGModel> plantIMGList;
    private List<ShowPlantCategory> plantCategoryList;
    private ShowStorePlantModel showStorePlantModel;
}
