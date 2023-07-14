package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.NotificationModels.ShowNotificationModel;
import com.example.thanhhoa.services.notification.NotificationService;
import com.example.thanhhoa.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private JwtUtil jwtUtil;

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
}
