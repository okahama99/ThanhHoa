package com.example.thanhhoa.dtos.PlantPriceModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowPlantPriceModel {
    private String id;
    private Double price;
    private LocalDateTime applyDate;
}
