package com.example.thanhhoa.dtos.PlantModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UpdateStorePlantModel implements Serializable {
    private String storePlantID;
    private Integer quantity;
    private String reason;
}
