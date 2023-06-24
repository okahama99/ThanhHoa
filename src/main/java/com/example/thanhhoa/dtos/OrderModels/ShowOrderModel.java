package com.example.thanhhoa.dtos.OrderModels;

import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
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
    private String progressStatus;
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
    private String storeID;
    private String staffID;
    private String customerID;
    private String distancePriceID;
    private List<ShowOrderDetailModel> detailList;
}
