package com.example.thanhhoa.services.notification;

import com.example.thanhhoa.dtos.NotificationModels.ShowNotificationModel;

import java.util.List;

public interface NotificationService {
    List<ShowNotificationModel> getByUserID(Long userID);
}
