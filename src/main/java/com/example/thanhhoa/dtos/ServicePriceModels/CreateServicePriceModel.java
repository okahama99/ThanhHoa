package com.example.thanhhoa.dtos.ServicePriceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateServicePriceModel implements Serializable {
    private Double price;
    private String serviceID;
}
