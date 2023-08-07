package com.example.thanhhoa.dtos.TransactionModels;

import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowTransactionModel implements Serializable {
    private String id;
    private String billNo;
    private String transNo;
    private String bankCode;
    private String cardType;
    private Integer amount;
    private String currency;
    private String reason;
    private LocalDateTime createdDate;
    private Status status;
    private ShowOrderModel showOrderModel;
    private ShowContractModel showContractModel;
    private ShowStaffModel showStaffModel;
}
