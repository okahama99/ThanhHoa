package com.example.thanhhoa.dtos.StoreModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowStorePlantModel implements Serializable {
    private String id;
    private Integer quantity;
}
