package com.example.thanhhoa.dtos.UserModels;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class RegisterUserModel implements Serializable{
        @NotBlank(message = "username is REQUIRED")
    @NotNull(message = "username is REQUIRED")
    @Size(max = 50, message = "Username must be atleast 10 and max 50 characters")
    private String username;

    @NotBlank(message = "password is REQUIRED")
    @NotNull(message = "password is REQUIRED")
    @Size(max = 50, message = "Password limit is 50 characters")
    private String password;

    @NotBlank(message = "fullName is REQUIRED")
    @NotNull(message = "fullName is REQUIRED")
    @Size(max = 50, message = "Fullname limit is 50 characters")
    private String fullName;

    @NotBlank(message = "email is REQUIRED")
    @NotNull(message = "email is REQUIRED")
    @Size(max = 50, message = "Email limit is 100 characters")
    private String email;

    @NotBlank(message = "phone is REQUIRED")
    @NotNull(message = "phone is REQUIRED")
    @Size(min = 10, max = 10, message = "Phone must be 10 numeric characters")
    private String phone;

    @NotBlank(message = "address is REQUIRED")
    @NotNull(message = "address is REQUIRED")
    private String address;

    private Boolean gender;

    private String avatar;
}
