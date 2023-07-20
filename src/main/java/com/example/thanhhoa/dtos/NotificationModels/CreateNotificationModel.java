package com.example.thanhhoa.dtos.NotificationModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class CreateNotificationModel implements Serializable {
    private String title;
    private Long userID;
    private String notificationID;
    private String content;
    private Map<String, String> data;
}
