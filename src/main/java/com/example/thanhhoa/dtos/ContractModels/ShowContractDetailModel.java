package com.example.thanhhoa.dtos.ContractModels;

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
    private LocalDateTime expectedEndDate;
    private String plantStatus;
    private String plantName;
    private Double price;
    private Double totalPrice;

    private ShowContractModel showContractModel;
    private ShowServiceTypeModel showServiceTypeModel;
    private ShowServiceModel showServiceModel;
    private ShowServicePackModel showServicePackModel;

    private List<ShowWorkingDateModel> workingDateList;
    private List<ShowPlantStatusIMGModel> plantStatusIMGModelList;
    private Double totalPage;
}
