package com.example.thanhhoa.dtos.ContractModels;

import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowContractDetailModel implements Serializable {
    private String id;
    private String timeWorking;
    private String note;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalPrice;
    private String contractID;
    private String serviceTypeID;
    private List<ShowReportModel> reportList;
    private List<ShowServiceTypeModel> serviceTypeList;
}
