package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowServiceModel implements Serializable {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Boolean atHome = false;
}
