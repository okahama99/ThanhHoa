package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPlantModel implements Serializable {
    private String id;
    private Integer quantity;
    private String plantName;
    private String plantPriceID;
    private Double plantPrice;
    private String image;
    private Double shipPrice;
}
