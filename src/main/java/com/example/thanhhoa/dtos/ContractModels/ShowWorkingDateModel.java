package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowWorkingDateModel implements Serializable {
    private String id;
    private LocalDateTime workingDate;
}