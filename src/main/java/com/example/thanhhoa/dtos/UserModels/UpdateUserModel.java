package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UpdateUserModel implements Serializable {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private Boolean gender;
    private String address;
    private String avatar;
}
