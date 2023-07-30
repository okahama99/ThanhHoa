package com.example.thanhhoa.dtos.ServicePriceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateServicePriceModel implements Serializable {
    private String id;
    private Double price;
    private String serviceID;
}
