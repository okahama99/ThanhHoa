package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePlantModel {
    private Long plantID;
    private String name;
    private String description;
    private String careNote;
    private Double height;
    private Double price;
    private Boolean withPot;

    private Integer quantity;

    private Long shipPriceID;
    private List<Long> categoryIDList;
}
