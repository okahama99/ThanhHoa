package com.example.thanhhoa.dtos.ReportModels;

import com.example.thanhhoa.dtos.ContractModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ContractModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ContractModels.ShowWorkingDateModel;
import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
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
    private String contractID;
    private String contractDetailID;
    private String timeWorking;

    private ShowWorkingDateModel showWorkingDateModel;
    private ShowServiceTypeModel showServiceTypeModel;
    private ShowServiceModel showServiceModel;
    private ShowCustomerModel showCustomerModel;
    private ShowStaffModel showStaffModel;
    private Double totalPage;
}
