package com.example.thanhhoa.dtos.ReportModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateReportModel implements Serializable {
    private String description;
    private String workingDateID;
}
