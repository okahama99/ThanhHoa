package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ApproveContractModel implements Serializable {
    private String contractID;
    private String paymentMethod;
    private Long staffID;
}
