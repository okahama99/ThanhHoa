package com.example.thanhhoa.dtos.ReportModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowReportModel implements Serializable {
    private String id;
    private String description;
    private String reason;
    private Status status;
    private LocalDateTime createdDate;
    private String workingDateID;
    private LocalDateTime workingDate;
    private String contractDetailID;
    private String serviceTypeID;
    private String serviceTypeName;
    private String serviceID;
    private String serviceName;
    private Long customerID;
    private String fullName;
    private String phone;
    private String email;
    private Long staffID;
    private String staffName;
    private String contractID;
    private String timeWorking;
    private Double totalPage;
}
