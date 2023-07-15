package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowDistancePriceModel implements Serializable {
    private String distancePriceID;
    private LocalDateTime dpApplyDate;
    private Double dpPricePerKm;
}
