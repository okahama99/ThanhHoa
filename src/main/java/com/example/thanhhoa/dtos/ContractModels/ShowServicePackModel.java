package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowServicePackModel implements Serializable {
    private String id;
    private String packRange;
    private Integer packPercentage;
    private LocalDateTime packApplyDate;
}
