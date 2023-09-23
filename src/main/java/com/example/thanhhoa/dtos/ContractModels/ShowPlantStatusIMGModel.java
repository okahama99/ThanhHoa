package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPlantStatusIMGModel implements Serializable {
    private String id;
    private String imgUrl;
}
