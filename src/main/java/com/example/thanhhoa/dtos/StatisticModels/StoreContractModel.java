package com.example.thanhhoa.dtos.StatisticModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StoreContractModel implements Serializable {
    private Integer numOfWorking;
    private Integer numOfDone;
    private Integer numOfContract;
    private String sumOfContract;
}
