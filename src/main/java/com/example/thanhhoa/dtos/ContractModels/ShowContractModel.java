package com.example.thanhhoa.dtos.ContractModels;

import com.example.thanhhoa.dtos.OrderModels.ShowCustomerModel;
import com.example.thanhhoa.dtos.OrderModels.ShowDistancePriceModel;
import com.example.thanhhoa.dtos.OrderModels.ShowPlantModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStoreModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowContractModel implements Serializable {
    //contract
    private String id;
    private String title;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private String reason;
    private LocalDateTime createdDate;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private LocalDateTime expectedEndedDate;
    private LocalDateTime approvedDate;
    private LocalDateTime rejectedDate;
    private Double total;
    private Boolean isFeedback;
    private Boolean isSigned;
    private Status status;
    private Boolean isPaid = false;
    private List<ShowContractIMGModel> imgList;

    private ShowStaffModel showStaffModel;
    private ShowCustomerModel showCustomerModel;
    private ShowStoreModel showStoreModel;

    private Double totalPage;
}
