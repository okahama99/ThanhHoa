package com.example.thanhhoa.dtos.DistancePriceModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowDistancePriceModel implements Serializable {
    private String distancePriceID;
    private Double pricePerKm;
    private LocalDateTime applyDate;
    private Status status;

}
