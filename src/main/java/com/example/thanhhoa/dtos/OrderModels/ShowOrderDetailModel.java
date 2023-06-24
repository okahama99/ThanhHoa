package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowOrderDetailModel implements Serializable {
    private String id;
    private Integer quantity;
    private Double price;
    private String orderID;
    private String plantID;
}
