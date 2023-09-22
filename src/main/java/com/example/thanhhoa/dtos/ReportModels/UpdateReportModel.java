package com.example.thanhhoa.dtos.ReportModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UpdateReportModel implements Serializable {
    private String id;
    private String description;
    private String workingDateID;
}
