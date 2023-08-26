package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.UserModels.LoginModel;
import com.example.thanhhoa.dtos.UserModels.RegisterStaffModel;
import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UpdateUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.services.firebase.CRUDUserFireBaseService;
import com.example.thanhhoa.services.google.GooglePojo;
import com.example.thanhhoa.services.google.GoogleUtils;
import com.example.thanhhoa.services.otp.OtpService;
import com.example.thanhhoa.services.user.UserService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GoogleUtils googleUtils;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> login(@RequestBody LoginModel loginModel) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<String, String> response = new HashMap<>(3);

            String token = jwtUtil.generateToken(authentication);

            response.put("status", "success");
            response.put("token", token);
            response.put("role", jwtUtil.getRoleNameFromJWT(token));
            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi đăng nhập.");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/loginWithEmailOrPhone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> loginWithEmailOrPhone(@RequestParam SearchType.LOGIN_WITH_EMAIL_OR_PHONE loginType,
                                                        @RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String phone) throws Exception {
        tblAccount user = null;
        switch(loginType) {
            case EMAIL:
                user = userService.loginWithEmail(email);
                if(user != null) {
                    Map<String, String> response = new HashMap<>(2);
                    String token = jwtUtil.generateTokenForEmailOrPhone(user);
                    response.put("status", "success");
                    response.put("token", token);
                    response.put("role", jwtUtil.getRoleNameFromJWT(token));

                    return ResponseEntity.ok().body(response);
                }
                break;
            case PHONE:
                user = userService.loginWithPhone(phone);
                if(user != null) {
                    Map<String, String> response = new HashMap<>(2);
                    String token = jwtUtil.generateTokenForEmailOrPhone(user);
                    response.put("status", "success");
                    response.put("token", token);
                    response.put("role", jwtUtil.getRoleNameFromJWT(token));

                    return ResponseEntity.ok().body(response);
                }
                break;
        }
        return ResponseEntity.badRequest().body("Lỗi đăng nhập, vui lòng kiểm tra lại thông tin của " + loginType + ".");
    }

    @RequestMapping("/loginGoogle")
    public ResponseEntity<Object> loginGoogle(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");

        if(code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Lỗi lấy code từ request.");
        }
        String accessToken = googleUtils.getToken(code);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        tblAccount user = googleUtils.buildUser(googlePojo);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getId()));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> response = new HashMap<>(2);

        String token = jwtUtil.generateToken(authentication);

        response.put("status", "Đăng nhập thành công.");
        response.put("token", token);
        response.put("role", jwtUtil.getRoleNameFromJWT(token));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/resetPasswordOTPEmail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> resetPasswordOTPEmail(@RequestParam("email") String email) throws MessagingException {

        Map<String, String> response = new HashMap<>(2);

        // generate OTP.
        Boolean isGenerated = otpService.generateOTPForResetPassword(email);
        if(!isGenerated) {
            response.put("status", "error");
            response.put("message", "Không thể khởi tạo OTP.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // success message
        response.put("status", "success");
        response.put("message", "Đã gửi mã OTP, xin vui lòng kiểm tra email.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/validateOTP", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> validateOTP(@RequestParam String email,
                                              @RequestParam Integer otp) {
        // validate provided OTP.
        Boolean isValid = otpService.validateOTP(email, otp);
        if(!isValid) {
            return new ResponseEntity<>("OTP không chính xác", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OTP chính xác", HttpStatus.OK);
    }

    @PostMapping(value = "/changePassword", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> changePassword(@RequestParam String email,
                                                 @RequestParam String password) {
        userService.resetPasswordUser(email, passwordEncoder.encode(password));
        return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> register(@RequestBody RegisterUserModel registerUserModel) {
        registerUserModel.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
        String result = userService.register(registerUserModel);
        if(result.equals("Lỗi tạo User.")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "/updateProfile", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateProfile(@RequestBody UpdateUserModel updateUserModel,
                                                HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")
                && !roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = userService.update(updateUserModel);
        if(result.equals("Cập nhật thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowUserModel> getAll(@RequestParam int pageNo,
                               @RequestParam int pageSize,
                               @RequestParam SearchType.USER sortBy,
                               @RequestParam(required = false, defaultValue = "true") Boolean sortTypeAsc) {
        Pageable paging;
        if(sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        return userService.getAll(paging);
    }

    @GetMapping(value = "/userFilter", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> userFilter(@RequestParam(required = false) String username,
                                             @RequestParam(required = false) String fullName,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) String phone,
                                             @RequestParam int pageNo,
                                             @RequestParam int pageSize,
                                             @RequestParam SearchType.USER sortBy,
                                             @RequestParam(required = false, defaultValue = "true") Boolean sortTypeAsc,
                                             HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        Pageable paging;
        if(sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsername(username, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullName(fullName, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByEmail(email, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByPhone(phone, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByEmailAndPhone(email, phone, paging));
        } else if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndFullName(username, fullName, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullNameAndEmail(fullName, email, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullNameAndPhone(fullName, phone, paging));
        } else if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndPhone(username, phone, paging));
        } else if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndEmail(username, email, paging));
        } else if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndFullNameAndEmailAndPhone(username, fullName, email, phone, paging));
        } else {
            return ResponseEntity.badRequest().body("Không thể tìm người dùng với giá trị đã nhập.");
        }
    }

    @GetMapping(value = "/searchCustomer", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> searchCustomer(@RequestParam(required = false) String username,
                                                 @RequestParam(required = false) String fullName,
                                                 @RequestParam(required = false) String email,
                                                 @RequestParam(required = false) String phone,
                                                 @RequestParam int pageNo,
                                                 @RequestParam int pageSize,
                                                 @RequestParam SearchType.USER sortBy,
                                                 @RequestParam(required = false, defaultValue = "true") Boolean sortTypeAsc,
                                                 HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        Pageable paging;
        if(sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameCus(username, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullNameCus(fullName, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByEmailCus(email, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByPhoneCus(phone, paging));
        } else if(StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByEmailAndPhoneCus(email, phone, paging));
        } else if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndFullNameCus(username, fullName, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullNameAndEmailCus(fullName, email, paging));
        } else if(StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByFullNameAndPhoneCus(fullName, phone, paging));
        } else if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndPhoneCus(username, phone, paging));
        } else if(!StringUtils.isEmpty(username) && StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndEmailCus(username, email, paging));
        } else if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(fullName)
                && !StringUtils.isEmpty(email) && !StringUtils.isEmpty(phone)) {
            return ResponseEntity.ok().body(userService.getUserByUsernameAndFullNameAndEmailAndPhoneCus(username, fullName, email, phone, paging));
        } else {
            return ResponseEntity.badRequest().body("Không thể tìm người dùng với giá trị đã nhập.");
        }
    }

    @GetMapping(value = "/getByToken", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getUserByToken(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        return ResponseEntity.ok().body(userService.getById(userId));
    }

    @GetMapping(value = "/getByID", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByID(@RequestParam Long userID,
                                          HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Admin") && !roleName.equalsIgnoreCase("Owner")
                && !roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Staff")
                && !roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return ResponseEntity.ok().body(userService.getById(userID));
    }

    @PostMapping(value = "/{username}", produces = "application/json;charset=UTF-8")
    public HttpStatus changeUserRole(@PathVariable(name = "username") String username,
                                     @RequestParam String roleId,
                                     HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        userService.changeUserRole(username, roleId);
        return HttpStatus.OK;

    }

    @PostMapping("/createFcmToken")
    public String createFcmToken(@RequestParam String fcmToken,
                                 @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        String username = jwtUtil.getUserNameFromJWT(jwt);
        UserFCMToken userFCMToken = new UserFCMToken();
        userFCMToken.setFcmToken(fcmToken);
        userFCMToken.setUsername(username);
        userService.updateUserFcmToken(userFCMToken);
        return CRUDUserFireBaseService.createUser(userFCMToken);
    }

    @PostMapping("/deleteFcmToken")
    public String deleteFcmToken(@RequestParam String username) throws InterruptedException, ExecutionException {
        userService.deleteUserFcmToken(username);
        tblAccount user = userRepository.getByUsername(username);
        UserFCMToken userFCMToken = new UserFCMToken();
        userFCMToken.setFcmToken(null);
        userFCMToken.setUsername(user.getUsername());
        return CRUDUserFireBaseService.updateUser(userFCMToken);
    }

    @PostMapping(value = "/tempPassword", produces = "application/json;charset=UTF-8")
    public String getTempPassword(@RequestBody RegisterStaffModel registerStaffModel,
                                  HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000);
        registerStaffModel.setPassword(passwordEncoder.encode(randomNum + ("")));
        String result = userService.generateTempPassword(registerStaffModel, registerStaffModel.getRoleName());
        if(result.equals("Tạo User thành công.")) {
            return randomNum + "";
        } else {
            return result;
        }
    }
}
