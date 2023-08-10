package com.example.thanhhoa.dtos.StatisticModels;

import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class ShowStatisticModel implements Serializable {
    private Integer numOfContractWorking;
    private Integer numOfContractDone;
    private Integer numOfContract;
    private String sumOfContract;

    private Integer numOfOrderApproved;
    private Integer numOfOrderPackaging;
    private Integer numOfOrderDelivering;
    private Integer numOfOrderReceived;
    private Integer numOfOrder;
    private String sumOfOrder;

    private ShowStoreModel showStoreModel;
    private StoreContractModel storeContractModel;
    private StoreOrderModel storeOrderModel;
}
