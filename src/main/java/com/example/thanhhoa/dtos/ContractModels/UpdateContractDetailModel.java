package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateContractDetailModel implements Serializable {
    private String id;
    private String note;
    private String timeWorking;
    private String startDate;
    private String endDate;
    private String servicePackID;
    private String serviceTypeID;
    private String plantName;
    @Nullable
    private String plantStatus;
    @Nullable
    private List<String> plantIMG;
}
