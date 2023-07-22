package com.example.thanhhoa.dtos.StoreModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowStoreModel implements Serializable {
    private String id;
    private String storeName;
    private String address;
    private String phone;
    private Status status;
    private String district;
    private Long managerID;
    private String managerName;
    private Double totalPage;
}
