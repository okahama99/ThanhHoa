package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetStaffModel implements Serializable{
    private Long staffID;
    private String staffName;
}
