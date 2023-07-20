package com.example.thanhhoa.dtos.OrderModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowOrderModel implements Serializable {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private Status progressStatus;
    private String reason;
    private LocalDateTime createdDate;
    private LocalDateTime packageDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime receivedDate;
    private LocalDateTime approveDate;
    private LocalDateTime rejectDate;
    private String latLong;
    private Double distance;
    private Double totalShipCost;
    private Double total;

    private ShowStaffModel showStaffModel;
    private ShowCustomerModel showCustomerModel;
    private ShowDistancePriceModel showDistancePriceModel;
    private ShowStoreModel showStoreModel;
    private List<ShowPlantModel> showPlantModel;

    private Integer numOfPlant;
    private Double totalPage;
}
