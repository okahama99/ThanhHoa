package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateContractDetailModel implements Serializable {
    private String note;
    private String timeWorking;
    private Double totalPrice;
    private String servicePackID;
    private String serviceTypeID;
}