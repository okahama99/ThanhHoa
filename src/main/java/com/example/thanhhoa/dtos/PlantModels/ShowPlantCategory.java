package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPlantCategory implements Serializable {
    private String categoryID;
    private String categoryName;
}
