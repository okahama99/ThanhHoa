package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserFCMToken implements Serializable {
    private String fcmToken;
    private Long accountID;
}
