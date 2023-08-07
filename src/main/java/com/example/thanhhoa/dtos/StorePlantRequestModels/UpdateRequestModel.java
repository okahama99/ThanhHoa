package com.example.thanhhoa.dtos.StorePlantRequestModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateRequestModel implements Serializable {
    private String requestID;
    private Long managerID;
    private String plantID;
    private String toStoreID;
    private Integer quantity;
    private String reason;
}
