package com.example.thanhhoa.services.firebase;

import com.example.thanhhoa.dtos.NotificationModels.CreateNotificationModel;
import com.example.thanhhoa.entities.Notification;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.repositories.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    UserRepository userRepository;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotification(CreateNotificationModel createNotificationModel) throws FirebaseMessagingException {

        tblAccount user = userRepository.getById(createNotificationModel.getUserID());

            com.google.firebase.messaging.Notification a = com.google.firebase.messaging.Notification
                    .builder()
                    .setTitle(createNotificationModel.getTitle())
                    .setBody(createNotificationModel.getContent())
                    .build();

            Message message = Message
                    .builder()
                    .setToken(user.getFcmToken())
                    .setNotification(a)
                    .putData("PLANT","P001")
                    .build();
            return firebaseMessaging.send(message);

    }

}
