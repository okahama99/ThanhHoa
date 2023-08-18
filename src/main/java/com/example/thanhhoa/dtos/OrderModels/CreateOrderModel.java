package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
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
    private String distancePriceID;
    private List<OrderDetailModel> detailList;

    @Nullable
    private Long staffID;
    @Nullable
    private String latLong;
    @Nullable
    private String transactionNo;
    @Nullable
    private Long customerID;
}
