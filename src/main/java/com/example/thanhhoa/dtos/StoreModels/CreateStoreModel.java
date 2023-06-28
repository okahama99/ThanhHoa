package com.example.thanhhoa.dtos.StoreModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateStoreModel implements Serializable {
    private String storeName;
    private String phone;
    private String address;
    private String districtID;
}
