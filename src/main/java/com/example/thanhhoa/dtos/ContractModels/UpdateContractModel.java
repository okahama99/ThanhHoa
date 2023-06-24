package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateContractModel implements Serializable {
    private String id;
    private String title;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private Double total;
    private String storeID;
    private String staffID;
    private String paymentTypeID;
}
