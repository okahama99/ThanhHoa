package com.example.thanhhoa.services.role;

import com.example.thanhhoa.dtos.RoleModels.ShowRoleModel;
import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.RoleRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private Util util;

    @Override
    public String create(String roleName) {
        Role role = new Role();
        Role lastRole = roleRepository.findFirstByOrderByIdDesc();
        if(lastRole == null) {
            role.setId(util.createNewID("R"));
        } else {
            role.setId(util.createIDFromLastID("R", 1, lastRole.getId()));
        }
        role.setRoleName(roleName);
        roleRepository.save(role);
        return "Tạo thành công.";
    }

    @Override
    public String update(String id, String roleName) {
        Role role = roleRepository.findByIdAndStatus(id, Status.ACTIVE);
        if(role == null){
            return "Không tìm thấy Vai trò với ID là " + id + ".";
        }
        role.setRoleName(roleName);
        roleRepository.save(role);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public String delete(String id) {
        Role role = roleRepository.findByIdAndStatus(id, Status.ACTIVE);
        if(role == null){
            return "Không tìm thấy Vai trò với ID là " + id + ".";
        }
        role.setStatus(Status.INACTIVE);
        roleRepository.save(role);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public List<ShowRoleModel> getAllRole() {
        List<Role> roleList = roleRepository.findAll();
        if(roleList == null){
            return null;
        }
        List<ShowRoleModel> modelList = new ArrayList<>();
        for(Role role : roleList) {
            ShowRoleModel model = new ShowRoleModel();
            model.setId(role.getId());
            model.setName(role.getRoleName());
            model.setStatus(role.getStatus());
            modelList.add(model);
        }
        return modelList;
    }

    @Override
    public ShowRoleModel getByID(String roleID) {
        Role role = roleRepository.findByIdAndStatus(roleID, Status.ACTIVE);
        if(role == null){
            return null;
        }
        ShowRoleModel model = new ShowRoleModel();
        model.setId(role.getId());
        model.setName(role.getRoleName());
        model.setStatus(role.getStatus());
        return model;
    }
}
