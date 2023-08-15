package com.example.thanhhoa.dtos.ServiceTypeModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
public class CreateServiceTypeModel implements Serializable {
    private String name;
    private String size;
    private String unit;
    private Integer percentage;
    @Nullable
    private String serviceID;
}
