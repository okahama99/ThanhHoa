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
    private Double totalPrice;
    private String contractID;
    private String serviceTypeID;
    private String typeName;
    private Integer typePercentage;
    private String typeSize;
    private LocalDateTime typeApplyDate;
    private String servicePackID;
    private String packRange;
    private Integer packPercentage;
    private LocalDateTime packApplyDate;
    private List<ShowWorkingDateModel> workingDateList;
}
