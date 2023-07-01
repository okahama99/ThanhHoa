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
    private Double distance;
    private Double totalShipCost;
    private Double total;
    private Status status;
    private String storeID;
    private Long staffID;
    private Long customerID;
    private String distancePriceID;
    private Double totalPage;
}
