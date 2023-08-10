package com.example.thanhhoa.dtos.StatisticModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StoreOrderModel implements Serializable {
    private Integer numOfApproved;
    private Integer numOfPackaging;
    private Integer numOfDelivering;
    private Integer numOfReceived;
    private Integer numOfOrder;
    private String sumOfOrder;
}
