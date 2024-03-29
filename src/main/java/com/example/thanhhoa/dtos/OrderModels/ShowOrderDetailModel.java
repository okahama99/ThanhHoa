package com.example.thanhhoa.dtos.OrderModels;

import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowOrderDetailModel implements Serializable {
    private String id;
    private Integer quantity;
    private Double price;
    private Boolean isFeedback;

    private ShowOrderModel showOrderModel;
    private ShowPlantModel showPlantModel;
    private ShowStaffModel showStaffModel;
    private ShowCustomerModel showCustomerModel;
    private ShowDistancePriceModel showDistancePriceModel;
    private ShowStoreModel showStoreModel;
    private ShowOrderFeedbackModel showOrderFeedbackModel;
    private Double totalPage;
}
