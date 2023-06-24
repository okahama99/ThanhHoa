package com.example.thanhhoa.dtos.StoreModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AddStoreEmployeeModel implements Serializable {
    private String storeID;
    private List<String> employeeIDList;
}
