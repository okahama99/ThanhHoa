package com.example.thanhhoa.dtos.UserModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowUserModel implements Serializable {
    private Long userID;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private Boolean gender;
    private Status status;
    private LocalDateTime createdDate;

    private String roleID;
    private String roleName;
    private Double totalPage;
}
