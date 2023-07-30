package com.example.thanhhoa.dtos.ServicePriceModels;

import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowServicePriceModel implements Serializable {
    private String id;
    private Double price;
    private LocalDateTime applyDate;
    private Status status;
    private ShowServiceModel serviceModel;
    private Double totalPage;
}
