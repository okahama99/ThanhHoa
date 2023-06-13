package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPlantCategory implements Serializable {
    private Long categoryID;
    private String categoryName;
}
