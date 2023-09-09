package com.example.thanhhoa.dtos.WorkingDateModels;

import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowWorkingDateModel implements Serializable {
    private String id;
    private LocalDateTime workingDate;
    private LocalDateTime startWorking;
    private LocalDateTime endWorking;
    private String startWorkingIMG;
    private String endWorkingIMG;
    private Status status;
    private String contractID;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String contractDetailID;
    private String timeWorking;
    private String note;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime expectedEndDate;
    private Double totalPrice;
    private String serviceID;
    private String serviceName;
    private String serviceTypeID;
    private String typeName;
    private Integer typePercentage;
    private String typeSize;
    private LocalDateTime typeApplyDate;
    private String servicePackID;
    private String packRange;
    private Integer packPercentage;
    private LocalDateTime packApplyDate;
    private ShowStaffModel showStaffModel;
    private Double totalPage;
}
