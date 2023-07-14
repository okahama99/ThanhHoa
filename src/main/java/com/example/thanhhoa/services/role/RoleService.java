package com.example.thanhhoa.services.role;

import com.example.thanhhoa.dtos.RoleModels.ShowRoleModel;

import java.util.List;

public interface RoleService {
    String create(String roleName);

    String update(String id, String roleName);

    String delete(String id);

    List<ShowRoleModel> getAllRole();

    ShowRoleModel getByID(String roleID);
}
