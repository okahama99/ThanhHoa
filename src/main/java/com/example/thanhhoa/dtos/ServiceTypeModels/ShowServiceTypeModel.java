package com.example.thanhhoa.dtos.ServiceTypeModels;

import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.enums.Status;
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
    private Status status;

    private ShowServiceModel showServiceModel;
    private Double totalPage;
}
