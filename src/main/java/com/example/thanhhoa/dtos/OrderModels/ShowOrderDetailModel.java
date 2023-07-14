package com.example.thanhhoa.dtos.OrderModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowOrderDetailModel implements Serializable {
    private String id;
    private Integer quantity;
    private Double price;
    private String orderID;
    private String plantID;
    private String plantName;
    private Double plantPrice;
    private String plantImage;
    private Double plantShipPrice;
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
    private Status status;
    private Long staffID;
    private Long customerID;
    private String distancePriceID;
    private LocalDateTime dpApplyDate;
    private Double dpPricePerKm;
    private String storeID;
    private String storeName;
    private String storeAddress;
    private String storePhone;
}
