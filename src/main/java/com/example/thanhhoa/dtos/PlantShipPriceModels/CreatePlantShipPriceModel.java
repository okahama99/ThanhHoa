package com.example.thanhhoa.dtos.PlantShipPriceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreatePlantShipPriceModel implements Serializable {
    private String potSize;
    private Double pricePerPlant;
}
