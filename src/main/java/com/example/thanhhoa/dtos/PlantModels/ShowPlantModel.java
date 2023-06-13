package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowPlantModel {
    private Long plantID;
    private String name;
    private Double height;
    private Double price;
    private Boolean withPot;
    private Double totalPage;
}
