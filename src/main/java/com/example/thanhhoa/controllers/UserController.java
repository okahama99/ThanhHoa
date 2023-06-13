package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.UserModels.RegisterUserModel;
import com.example.thanhhoa.dtos.UserModels.ShowUserModel;
import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.UserPagingRepository;
import com.example.thanhhoa.services.firebase.CRUDUserFireBaseService;
import com.example.thanhhoa.services.google.GooglePojo;
import com.example.thanhhoa.services.google.GoogleUtils;
import com.example.thanhhoa.services.otp.OtpService;
import com.example.thanhhoa.services.user.UserService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private UserPagingRepository userPagingRepository;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> login(@RequestParam String username,
                                        @RequestParam String password) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Map<String, String> response = new HashMap<>(2);

            String token = jwtUtil.generateToken(authentication);

            response.put("status", "success");
            response.put("token", token);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi đăng nhập.");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/loginWithEmailOrPhone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> loginWithEmailOrPhone(@RequestParam SearchType.LOGIN_WITH_EMAIL_OR_PHONE loginType,
                                                        @RequestParam(required = false) String email,
                                                        @RequestParam(required = false) String phone) throws Exception {
        tblAccount user = null;
        switch (loginType) {
            case EMAIL:
                user = userService.loginWithEmail(email);
                if (user != null) {
                    Map<String, String> response = new HashMap<>(2);
                    String token = jwtUtil.generateTokenForEmailOrPhone(user);
                    response.put("status", "success");
                    response.put("token", token);

                    return ResponseEntity.ok().body(response);
                }
                break;
            case PHONE:
                user = userService.loginWithPhone(phone);
                if (user != null) {
                    Map<String, String> response = new HashMap<>(2);
                    String token = jwtUtil.generateTokenForEmailOrPhone(user);
                    response.put("status", "success");
                    response.put("token", token);

                    return ResponseEntity.ok().body(response);
                }
                break;
        }
        return ResponseEntity.badRequest().body("Lỗi đăng nhập, vui lòng kiểm tra lại thông tin của " + loginType + ".");
    }

    @RequestMapping("/login-google")
    public ResponseEntity<Object> loginGoogle(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Lỗi lấy code từ request.");
        }
        String accessToken = googleUtils.getToken(code);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        tblAccount user = googleUtils.buildUser(googlePojo);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getId().toString()));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> response = new HashMap<>(2);

        String token = jwtUtil.generateToken(authentication);

        response.put("status", "Đăng nhập thành công.");
        response.put("token", token);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/resetPasswordOTPEmail", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> resetPasswordOTPEmail(@RequestParam("email") String email) throws MessagingException {

        Map<String, String> response = new HashMap<>(2);

        // generate OTP.
        Boolean isGenerated = otpService.generateOTPForResetPassword(email);
        if (!isGenerated) {
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
        if (!isValid) {
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
        if (result.equals("Tạo User thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }


    @GetMapping(value = "/findUserByRoleName", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> findUserByRoleName(@RequestParam String roleName,
                                                     @RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam SearchType.USER sortBy,
                                                     @RequestParam boolean sortTypeAsc,
                                                     @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
        Pageable paging;
        if (sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if (sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        Page<tblAccount> userList = userPagingRepository.findByRole_RoleNameLike(roleName, paging);
        if (userList == null) {
            return ResponseEntity.badRequest()
                    .body("No User found with input: '" + roleName + "'. ");
        }
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(value = "/findUserByStatus", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> findUserByStatus(@RequestParam SearchType.STATUS status,
                                                   @RequestParam int pageNo,
                                                   @RequestParam int pageSize,
                                                   @RequestParam SearchType.USER sortBy,
                                                   @RequestParam boolean sortTypeAsc,
                                                   @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
        Pageable paging;
        if (sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if (sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        Page<tblAccount> userList = null;
        switch (status) {
            case ACTIVE:
                userList = userPagingRepository.findByStatus(Status.ACTIVE, paging);
                if (userList == null) {
                    return ResponseEntity.badRequest()
                            .body("No User found with input: '" + status + "'. ");
                }
                break;
            case INACTIVE:
                userList = userPagingRepository.findByStatus(Status.INACTIVE, paging);
                if (userList == null) {
                    return ResponseEntity.badRequest()
                            .body("No User found with input: '" + status + "'. ");
                }
                break;
        }
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("/findUserByFullName")
    public ResponseEntity<Object> findUserByFullName(@RequestParam String fullName,
                                                     @RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam SearchType.USER sortBy,
                                                     @RequestParam boolean sortTypeAsc,
                                                     @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
        Pageable paging;
        if (sortBy.equals("FULLNAME")) {
            paging = util.makePaging(pageNo, pageSize, "fullName", sortTypeAsc);
        } else if (sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortTypeAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortTypeAsc);
        }
        Page<tblAccount> userList = userPagingRepository.findByFullNameLike(fullName, paging);
        if (userList == null) {
            return ResponseEntity.badRequest()
                    .body("No User found with input: '" + fullName + "'. ");
        }
        return ResponseEntity.ok().body(userList);
    }

    @GetMapping(path = "/getUserByToken", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getUserByToken(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token){
        ShowUserModel userModel = userService.getUserByID(jwtUtil.getUserIdFromJWT(token));
        return ResponseEntity.ok().body(userModel);
    }

    @PostMapping(value = "/changeUserRole/{userid}", produces = "application/json;charset=UTF-8")
    public HttpStatus changeUserRole(@PathVariable(name = "userid") Long userid,
                                     @RequestParam Long roleId,
                                     @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null){
            throw new IllegalArgumentException("Invalid jwt.");
        }
        userService.changeUserRole(userid, roleId);
        return HttpStatus.OK;

    }

    @PostMapping("/createFcmToken")
    public String createFcmToken(@RequestBody UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
        userService.updateUserFcmToken(userFCMToken);
        return CRUDUserFireBaseService.saveUser(userFCMToken);
    }

    @PostMapping("/deleteFcmToken")
    public String deleteFcmToken(@RequestParam Long userid) throws InterruptedException, ExecutionException {
        userService.deleteUserFcmToken(userid);
        tblAccount user = userRepository.getById(userid);
        UserFCMToken userFCMToken = new UserFCMToken();
        userFCMToken.setFcmToken(null);
        userFCMToken.setAccountID(user.getId());
        userFCMToken.setUserName(user.getUsername());
        userFCMToken.setFullName(user.getFullName());
        userFCMToken.setAvatar(user.getAvatar());
        userFCMToken.setEmail(user.getEmail());
        userFCMToken.setPhone(user.getPhone());
        return CRUDUserFireBaseService.updateUser(userFCMToken);
    }
}
