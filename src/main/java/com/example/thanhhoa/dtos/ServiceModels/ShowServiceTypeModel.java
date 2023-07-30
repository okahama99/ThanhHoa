package com.example.thanhhoa.dtos.ServiceModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowServiceTypeModel implements Serializable {
    private String id;
    private String name;
    private String size;
    private String unit;
    private Integer percentage;
    private LocalDateTime applyDate;
    private String serviceID;
}
