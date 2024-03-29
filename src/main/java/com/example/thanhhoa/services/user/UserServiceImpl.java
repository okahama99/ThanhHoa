package com.example.thanhhoa.services.user;

import com.example.thanhhoa.dtos.UserModels.AuthorizeModel;
import com.example.thanhhoa.dtos.UserModels.RegisterStaffModel;
import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UpdateUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.Role;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.RoleRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.UserPagingRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private Util util;

    @Override
    public AuthorizeModel getById(Long userID) {
        tblAccount user = userRepository.findById(userID).get();
        AuthorizeModel model = new AuthorizeModel();
        model.setUserID(user.getId());
        model.setUsername(user.getUsername());
        model.setFullName(user.getFullName());
        model.setEmail(user.getEmail());
        model.setPhone(user.getPhone());
        model.setRoleID(user.getRole().getId());
        model.setRoleName(user.getRole().getRoleName());
        model.setAvatar(user.getAvatar());
        model.setAddress(user.getAddress());
        model.setGender(user.getGender());
        model.setStatus(user.getStatus());
        StoreEmployee storeEmployee = storeEmployeeRepository.findByAccount_IdAndStatus(user.getId(), Status.ACTIVE);
        if((user.getRole().getRoleName().equalsIgnoreCase("Manager") || user.getRole().getRoleName().equalsIgnoreCase("Staff")) && storeEmployee != null) {
            model.setStoreID(storeEmployee.getStore().getId());
            model.setStoreName(storeEmployee.getStore().getStoreName());
        }
        return model;
    }

    @Override
    public AuthorizeModel getByUsername(String username) {
        tblAccount user = userRepository.getByUsername(username);
        AuthorizeModel model = new AuthorizeModel();
        model.setUserID(user.getId());
        model.setUsername(user.getUsername());
        model.setPassword(user.getPassword());
        model.setFullName(user.getFullName());
        model.setEmail(user.getEmail());
        model.setPhone(user.getPhone());
        model.setRoleID(user.getRole().getId());
        model.setRoleName(user.getRole().getRoleName());
        model.setAvatar(user.getAvatar());
        model.setAddress(user.getAddress());
        model.setGender(user.getGender());
        model.setStatus(user.getStatus());
        StoreEmployee storeEmployee = storeEmployeeRepository.findByAccount_IdAndStatus(user.getId(), Status.ACTIVE);
        if((user.getRole().getRoleName().equalsIgnoreCase("Manager") || user.getRole().getRoleName().equalsIgnoreCase("Staff")) && storeEmployee != null) {
            model.setStoreID(storeEmployee.getStore().getId());
            model.setStoreName(storeEmployee.getStore().getStoreName());
        }
        return model;
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
        if(user == null) {
            return null;
        }
        return user;
    }

    @Override
    public tblAccount loginWithPhone(String phone) {
        tblAccount user = userRepository.getByPhone(phone);
        if(user == null) {
            return null;
        }
        return user;
    }

    @Override
    public String register(RegisterUserModel registerUserModel) {
        if(userRepository.getByUsername(registerUserModel.getUsername()) != null) {
            return "Username đã tồn tại.";
        }
        if(userRepository.getByEmail(registerUserModel.getEmail()) != null) {
            return "Email đã tồn tại.";
        }
        if(userRepository.getByPhone(registerUserModel.getPhone()) != null) {
            return "Phone đã tồn tại.";
        }
        Role role = roleRepository.getRoleByRoleName("Customer");
        tblAccount user = new tblAccount();
        user.setUsername(registerUserModel.getUsername());
        user.setPassword(registerUserModel.getPassword());
        user.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setFullName(registerUserModel.getFullName());
        user.setEmail(registerUserModel.getEmail());
        user.setAddress(registerUserModel.getAddress());
        user.setGender(registerUserModel.getGender());
        user.setAvatar(registerUserModel.getAvatar());
        user.setPhone(registerUserModel.getPhone());
        user.setStatus(Status.ACTIVE);
        user.setRole(role);
        if(userRepository.saveAndFlush(user) != null) {
            return user.getId().toString();
        } else {
            return "Lỗi tạo User.";
        }
    }

    @Override
    public void changeUserRole(String username, String roleId) {
        tblAccount user = userRepository.getByUsername(username);
        Role role = roleRepository.getById(roleId);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void updateUserFcmToken(UserFCMToken userFCMToken) {
        tblAccount user = userRepository.getByUsername(userFCMToken.getUsername());
        user.setFcmToken(userFCMToken.getFcmToken());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUserFcmToken(String username) {
        tblAccount user = userRepository.getByUsername(username);
        user.setFcmToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public String generateTempPassword(RegisterStaffModel registerStaffModel, String roleName) {
        if(userRepository.getByUsername(registerStaffModel.getUsername()) != null) {
            return "Username đã tồn tại.";
        }
        Role role = roleRepository.getRoleByRoleName(roleName);
        if(role == null) {
            return "Sai tên Role.";
        }
        if(!role.getRoleName().equals("Owner") && !role.getRoleName().equals("Manager") && !role.getRoleName().equals("Staff")) {
            return "Tên Role phải là Owner hoặc Manager hoặc Staff";
        }
        tblAccount user = new tblAccount();
        user.setUsername(registerStaffModel.getUsername());
        user.setPassword(registerStaffModel.getPassword());
        user.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        user.setFullName(registerStaffModel.getFullName());
        user.setGender(registerStaffModel.getGender());
        user.setAvatar(registerStaffModel.getAvatar());
        user.setRole(role);
        user.setStatus(Status.ACTIVE);

        if(userRepository.saveAndFlush(user) != null) {
            return "Tạo User thành công.";
        } else {
            return "Lỗi tạo User.";
        }
    }

    @Override
    public String update(UpdateUserModel updateUserModel) {
        tblAccount user = userRepository.findByIdAndStatus(updateUserModel.getId(), Status.ACTIVE);
        if(user == null){
            return "Không tìm thấy Account với ID là " + updateUserModel.getId() + ".";
        }
        user.setAddress(updateUserModel.getAddress());
        user.setPhone(updateUserModel.getPhone());
        user.setEmail(updateUserModel.getEmail());
        user.setGender(updateUserModel.getGender());
        user.setFullName(updateUserModel.getFullName());
        user.setAvatar(updateUserModel.getAvatar());
        userRepository.save(user);
        return "Cập nhật thành công.";
    }

    @Override
    public List<ShowUserModel> getAll(Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findAll(paging);
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

    // ---------------------------------------------------------------------------------------------------------

    @Override
    public List<ShowUserModel> getAllCus(Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findAllByRole_RoleName("CUSTOMER",paging);
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullNameCus(String fullName, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndStatusAndRole_RoleName(fullName, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameCus(String username, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndStatusAndRole_RoleName(username, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByEmailCus(String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByEmailContainingAndStatusAndRole_RoleName(email, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByPhoneCus(String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByPhoneAndStatusAndRole_RoleName(phone, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndFullNameCus(String username, String fullName, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndFullNameContainingAndStatusAndRole_RoleName(username, fullName, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullNameAndEmailCus(String fullName, String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndEmailContainingAndStatusAndRole_RoleName(fullName, email, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByFullNameAndPhoneCus(String fullName, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByFullNameContainingAndPhoneAndStatusAndRole_RoleName(fullName, phone, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndEmailCus(String username, String email, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndEmailContainingAndStatusAndRole_RoleName(username, email, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndPhoneCus(String username, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndPhoneAndStatusAndRole_RoleName(username, phone, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByEmailAndPhoneCus(String email, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByEmailContainingAndPhoneAndStatusAndRole_RoleName(email, phone, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }

    @Override
    public List<ShowUserModel> getUserByUsernameAndFullNameAndEmailAndPhoneCus(String username, String fullName, String email, String phone, Pageable paging) {
        Page<tblAccount> pagingResult = userPagingRepository.findByUsernameContainingAndFullNameContainingAndEmailContainingAndPhoneAndStatusAndRole_RoleName(username, fullName, email, phone, paging, Status.ACTIVE, "CUSTOMER");
        return util.userPagingConverter(pagingResult, paging);
    }
}
