package com.example.thanhhoa.dtos.OrderModels;

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
}
