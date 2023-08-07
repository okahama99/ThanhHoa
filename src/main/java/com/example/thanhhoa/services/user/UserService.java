package com.example.thanhhoa.services.user;

import com.example.thanhhoa.dtos.UserModels.AuthorizeModel;
import com.example.thanhhoa.dtos.UserModels.RegisterStaffModel;
import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UpdateUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.tblAccount;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    AuthorizeModel getById(Long userID);

    AuthorizeModel getByUsername(String username);

    void resetPasswordUser(String email, String password);

    tblAccount loginWithEmail(String email);

    tblAccount loginWithPhone(String phone);

    String register(RegisterUserModel registerUserModel);

    String generateTempPassword(RegisterStaffModel registerStaffModel, String roleName);

    String update(UpdateUserModel updateUserModel);

    void changeUserRole(String username, String roleId);

    void updateUserFcmToken(UserFCMToken userFCMToken);

    void deleteUserFcmToken(String username);

    List<ShowUserModel> getAll(Pageable paging);

    List<ShowUserModel> getUserByFullName(String fullName, Pageable paging);

    List<ShowUserModel> getUserByUsername(String username, Pageable paging);

    List<ShowUserModel> getUserByEmail(String username, Pageable paging);

    List<ShowUserModel> getUserByPhone(String username, Pageable paging);

    List<ShowUserModel> getUserByUsernameAndFullName(String username, String fullName, Pageable paging);

    List<ShowUserModel> getUserByFullNameAndEmail(String fullName, String email, Pageable paging);

    List<ShowUserModel> getUserByFullNameAndPhone(String fullName, String phone, Pageable paging);

    List<ShowUserModel> getUserByUsernameAndEmail(String username, String email, Pageable paging);

    List<ShowUserModel> getUserByUsernameAndPhone(String username, String phone, Pageable paging);

    List<ShowUserModel> getUserByEmailAndPhone(String email, String phone, Pageable paging);

    List<ShowUserModel> getUserByUsernameAndFullNameAndEmailAndPhone(String username, String fullName, String email, String phone, Pageable paging);


}
