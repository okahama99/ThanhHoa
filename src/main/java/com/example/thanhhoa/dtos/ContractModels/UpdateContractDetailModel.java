package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateContractDetailModel implements Serializable {
    private String id;
    private String note;
    private String timeWorking;
    private String startDate;
    private String servicePackID;
    private String serviceTypeID;
    private String plantStatus;
    private String plantIMG;
}
