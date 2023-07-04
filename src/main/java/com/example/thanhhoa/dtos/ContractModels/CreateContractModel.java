package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateContractModel implements Serializable {
    private String title;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String storeID;
    private String customerID;
    private List<CreateContractDetailModel> detailModelList;
}
