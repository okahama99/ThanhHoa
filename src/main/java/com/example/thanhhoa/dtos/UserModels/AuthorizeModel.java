package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthorizeModel implements Serializable {
    private Long userID;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String roleID;
    private String roleName;
    private String avatar;
}
