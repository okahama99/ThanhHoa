package com.example.thanhhoa.dtos.RoleModels;

import com.example.thanhhoa.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShowRoleModel implements Serializable {
    private String id;
    private String name;
    private Status status;
}
