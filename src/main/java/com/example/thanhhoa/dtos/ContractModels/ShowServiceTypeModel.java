package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowServiceTypeModel implements Serializable {
    private String id;
    private String typeName;
    private Integer typePercentage;
    private String typeSize;
    private String typeUnit;
    private LocalDateTime typeApplyDate;
}
