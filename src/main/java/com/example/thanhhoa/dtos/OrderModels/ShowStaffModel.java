package com.example.thanhhoa.dtos.OrderModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowStaffModel implements Serializable {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Long id;
}
