package com.example.thanhhoa.dtos.ReportModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowReportModel implements Serializable {
    private String id;
    private String description;
    private LocalDateTime createdDate;
    private String contractDetailID;
    private String customerID;
}