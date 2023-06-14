package com.example.thanhhoa.services.user;

import com.example.thanhhoa.dtos.UserModels.RegisterStaffModel;
import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.RoleRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.UserPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserPagingRepository userPagingRepository;
    @Autowired
    private Util util;


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
        if (userRepository.getByUsername(registerUserModel.getUsername()) != null) {
            return "Username đã tồn tại.";
        }
        if (userRepository.getByEmail(registerUserModel.getEmail()) != null) {
            return "Email đã tồn tại.";
        }
        if (userRepository.getByPhone(registerUserModel.getPhone()) != null) {
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
        if (userRepository.saveAndFlush(user) != null) {
            return "Tạo User thành công.";
        } else {
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
    public String generateTempPassword(RegisterStaffModel registerStaffModel, String roleName) {
        if (userRepository.getByUsername(registerStaffModel.getUsername()) != null) {
            return "Username đã tồn tại.";
        }
        Role role = roleRepository.getRoleByRoleName(roleName);
        if (role == null) {
            return "Sai tên Role.";
        }
        tblAccount user = new tblAccount();
        user.setUsername(registerStaffModel.getUsername());
        user.setPassword(registerStaffModel.getPassword());
        user.setCreatedDate(LocalDateTime.now());
        user.setFullName(registerStaffModel.getFullName());
        user.setGender(registerStaffModel.getGender());
        user.setAvatar(registerStaffModel.getAvatar());
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        if (userRepository.saveAndFlush(user) != null) {
            return "Tạo User thành công.";
        } else {
            return "Lỗi tạo User.";
        }
    }

    @Override
    public List<ShowUserModel> getAll(Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findAllByStatus(paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullName(String fullName, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndStatus(fullName, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsername(String username, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndStatus(username, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByEmail(String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByEmailContainingAndStatus(email, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByPhone(String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByPhoneAndStatus(phone, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndFullName(String username, String fullName, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndFullNameContainingAndStatus(username, fullName, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullNameAndEmail(String fullName, String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndEmailContainingAndStatus(fullName, email, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullNameAndPhone(String fullName, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndPhoneAndStatus(fullName, phone, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndEmail(String username, String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndEmailContainingAndStatus(username, email, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndPhone(String username, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndPhoneAndStatus(username, phone, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByEmailAndPhone(String email, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByEmailContainingAndPhoneAndStatus(email, phone, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndFullNameAndEmailAndPhone(String username, String fullName, String email, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndFullNameContainingAndEmailContainingAndPhoneAndStatus(username, fullName, email, phone, paging, Status.ACTIVE);
        return util.userPagingConverter(pagingResult, paging);
    }
}
