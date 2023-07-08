package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowPaymentTypeModel implements Serializable {
    private String id;
    private String name;
    private Integer value;
}
