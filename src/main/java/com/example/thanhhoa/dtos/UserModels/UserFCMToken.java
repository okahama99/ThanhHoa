package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserFCMToken implements Serializable {
    private String fcmToken;
    private Long accountID;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
}
