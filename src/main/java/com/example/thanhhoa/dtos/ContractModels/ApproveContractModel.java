package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ApproveContractModel implements Serializable {
    private String contractID;
    private LocalDateTime startedDate;
    private LocalDateTime approvedDate;
    private Double deposit;
    private String paymentMethod;
    private Long staffID;
    private String paymentTypeID;
}
