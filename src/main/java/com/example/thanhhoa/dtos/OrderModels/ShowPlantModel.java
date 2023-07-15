package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPlantModel implements Serializable {
    private String id;
    private String plantName;
    private Double plantPrice;
    private String plantImage;
    private Double plantShipPrice;
}
