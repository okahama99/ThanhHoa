package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreateManagerContractModel implements Serializable {
    private String title;
    private String fullName;
    private String phone;
    private String address;
    private String storeID;
    private Double deposit;
    private String paymentMethod;
    private Long staffID;
    private String paymentTypeID;
    private List<CreateContractDetailModel> detailModelList;
    @Nullable
    private Long customerID;
    @Nullable
    private String email;
    @Nullable
    private List<String> listURL;
    @Nullable
    private Boolean isPaid = false;
}
