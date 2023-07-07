package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateContractModel implements Serializable {
    private String id;
    private String title;
    private String fullName;
    private String phone;
    private String address;
    private Double total;
    private String storeID;
    private Long staffID;

    @Nullable
    private String email;
}
