package com.example.thanhhoa.dtos.StoreModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateStoreModel implements Serializable {
    private String storeID;
    private String storeName;
    private String phone;
    private String address;
    private String districtID;
}
