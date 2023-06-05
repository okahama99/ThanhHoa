package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFCMToken {
    private String fcmToken;
    private Long userID;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
}
