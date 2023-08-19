package com.example.thanhhoa.dtos.TransactionModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
public class CreateTransactionModel implements Serializable {
    private Integer amount;
    private String currency;
    private String reason;
    @Nullable
    private String orderID;
    @Nullable
    private String contractID;
    @Nullable
    private Long userID;

}
