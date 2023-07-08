package com.example.thanhhoa.dtos.ServicePackModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowServicePackModel implements Serializable {
    private String id;
    private String range;
    private Integer percentage;
    private LocalDateTime applyDate;
    private Status status;
}
