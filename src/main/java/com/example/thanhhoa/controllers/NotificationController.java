package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.NotificationModels.CreateNotificationModel;
import com.example.thanhhoa.dtos.NotificationModels.ShowNotificationModel;
import com.example.thanhhoa.services.firebase.FirebaseMessagingService;
import com.example.thanhhoa.services.notification.NotificationService;
import com.example.thanhhoa.utils.JwtUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody CreateNotificationModel createNotificationModel) throws FirebaseMessagingException {
        return firebaseMessagingService.sendNotification(createNotificationModel);
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowNotificationModel> getNotification(HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        List<ShowNotificationModel> list = notificationService.getByUserID(jwtUtil.getUserIDFromRequest(request));
        return list;
    }

    @PutMapping(value = "/isRead", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> isRead(@RequestParam String notificationID) {
        String result = notificationService.isRead(notificationID);
        if(result.equals("Cập nhật thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/isReadByToken", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> isReadByToken(HttpServletRequest request) {
        String result = notificationService.isReadAllByUserID(jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Cập nhật thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}
