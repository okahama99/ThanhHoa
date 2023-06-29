package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreateOrderModel implements Serializable {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private Double distance;
    private String storeID;
    private Long staffID;
    private String distancePriceID;

    private List<OrderDetailModel> detailList;
}
