package com.example.thanhhoa.services.user;

import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.tblAccount;

public interface UserService {
    tblAccount getByUsername(String username);

    tblAccount getById(Long userID);

    void resetPasswordUser(String email, String password);

    tblAccount loginWithEmail(String email);

    tblAccount loginWithPhone(String phone);

    String register(RegisterUserModel registerUserModel);

    void changeUserRole(Long userId, Long roleId);

    void updateUserFcmToken(UserFCMToken userFCMToken);

    void deleteUserFcmToken(Long userid);

    ShowUserModel getUserByID(Long userID);
}
