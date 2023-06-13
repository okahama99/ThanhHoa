package com.example.thanhhoa.services.user;

import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.RoleRepository;
import com.example.thanhhoa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public tblAccount getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public tblAccount getById(Long userID) {
        return userRepository.getById(userID);
    }

    @Override
    public void resetPasswordUser(String email, String password) {
        tblAccount user = userRepository.findByEmailAndStatus(email, Status.ACTIVE);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public tblAccount loginWithEmail(String email) {
        tblAccount user = userRepository.getByEmail(email);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public tblAccount loginWithPhone(String phone) {
        tblAccount user = userRepository.getByPhone(phone);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public String register(RegisterUserModel registerUserModel) {
        if(userRepository.getByUsername(registerUserModel.getUsername())!=null){
            return "Username đã tồn tại.";
        }
        if(userRepository.getByEmail(registerUserModel.getEmail())!=null){
            return "Email đã tồn tại.";
        }
        if(userRepository.getByPhone(registerUserModel.getPhone())!=null){
            return "Phone đã tồn tại.";
        }
        Role role = roleRepository.getRoleByRoleName("Customer");
        tblAccount user = new tblAccount();
        user.setUsername(registerUserModel.getUsername());
        user.setPassword(registerUserModel.getPassword());
        user.setCreatedDate(LocalDateTime.now());
        user.setFullName(registerUserModel.getFullName());
        user.setEmail(registerUserModel.getEmail());
        user.setAddress(registerUserModel.getAddress());
        user.setGender(registerUserModel.getGender());
        user.setAvatar(registerUserModel.getAvatar());
        user.setPhone(registerUserModel.getPhone());
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        if(userRepository.saveAndFlush(user)!=null){
            return "Tạo User thành công.";
        }else{
            return "Lỗi tạo User.";
        }
    }

    @Override
    public void changeUserRole(Long userId, Long roleId) {
        tblAccount user = userRepository.getById(userId);
        Role role = roleRepository.getById(roleId);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void updateUserFcmToken(UserFCMToken userFCMToken) {
        tblAccount user = userRepository.getById(userFCMToken.getAccountID());
        user.setFcmToken(userFCMToken.getFcmToken());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUserFcmToken(Long userId) {
        tblAccount user = userRepository.getById(userId);
        user.setFcmToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public ShowUserModel getUserByID(Long userID) {
        tblAccount user = userRepository.getById(userID);
        ShowUserModel userModel = new ShowUserModel();
        userModel.setUserName(user.getUsername());
        userModel.setFullName(user.getFullName());
        userModel.setAddress(user.getAddress());
        userModel.setCreatedDate(user.getCreatedDate());
        userModel.setGender(user.getGender());
        userModel.setAvatar(user.getAvatar());
        userModel.setPhone(user.getPhone());
        userModel.setEmail(user.getEmail());
        return userModel;
    }
}
