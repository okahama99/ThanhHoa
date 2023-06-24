package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowContractModel implements Serializable {
    private String id;
    private String title;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private String reason;
    private LocalDateTime createdDate;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private LocalDateTime approvedDate;
    private LocalDateTime rejectedDate;
    private Double deposit;
    private Double total;
    private Boolean isFeedback;
    private Boolean isSigned;
    private String storeID;
    private String staffID;
    private String customerID;
    private String paymentTypeID;
    private List<ShowContractDetailModel> contractDetailList;
}
