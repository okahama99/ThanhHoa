package com.example.thanhhoa.dtos.StoreModels;

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
    private String district;
    private Long managerID;
    private String managerName;
    private Double totalPage;
}
