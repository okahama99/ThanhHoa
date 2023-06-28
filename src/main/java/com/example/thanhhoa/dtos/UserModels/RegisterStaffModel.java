package com.example.thanhhoa.dtos.UserModels;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
public class RegisterStaffModel implements Serializable {
    @NotBlank(message = "username is REQUIRED")
    @NotNull(message = "username is REQUIRED")
    @Size(max = 50, message = "Username must be atleast 10 and max 50 characters")
    private String username;

    @Schema(example = "DO NOT INPUT INTO THIS FIELD") /* Hint for Swagger */
    @Nullable
    private String password;

    @NotBlank(message = "fullName is REQUIRED")
    @NotNull(message = "fullName is REQUIRED")
    @Size(max = 50, message = "Fullname limit is 50 characters")
    private String fullName;

    private Boolean gender;

    private String avatar;

    @Schema(example = "Must be Owner or Manager or Staff")
    private String roleName;
}
