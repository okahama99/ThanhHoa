package com.example.thanhhoa.dtos.NotificationModels;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowNotificationModel implements Serializable {
    private String id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime date;
}
