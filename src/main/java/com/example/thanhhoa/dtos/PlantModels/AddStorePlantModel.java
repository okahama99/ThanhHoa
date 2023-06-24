package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddStorePlantModel implements Serializable {
    private String storeID, plantID;
    private Integer quantity;
}
