package com.example.thanhhoa.dtos.ContractModels;

import com.example.thanhhoa.enums.Status;
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
    private String storeName;
    private Long staffID;
    private String staffName;
    private Long customerID;
    private String paymentTypeID;
    private Status status;
    private List<ShowContractIMGModel> imgList;
    private Double totalPage;
}
