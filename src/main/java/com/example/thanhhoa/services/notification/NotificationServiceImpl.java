package com.example.thanhhoa.services.notification;

import com.example.thanhhoa.dtos.NotificationModels.ShowNotificationModel;
import com.example.thanhhoa.entities.Notification;
import com.example.thanhhoa.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<ShowNotificationModel> getByUserID(Long userID) {
        List<Notification> notificationList = notificationRepository.findByTblAccount_IdOrderByDateDesc(userID);
        if(notificationList == null){
            return null;
        }
        List<ShowNotificationModel> modelList = new ArrayList<>();
        for(Notification notification : notificationList) {
            ShowNotificationModel model = new ShowNotificationModel();
            model.setId(notification.getId());
            model.setDate(notification.getDate());
            model.setDescription(notification.getDescription());
            model.setLink(notification.getLink());
            model.setTitle(notification.getTitle());
            modelList.add(model);
        }
        return modelList;
    }
}
