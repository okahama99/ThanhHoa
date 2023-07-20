package com.example.thanhhoa.services.firebase;

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


    public String sendNotification(Notification notification) throws FirebaseMessagingException {

        tblAccount user = userRepository.getById(notification.getTblAccount().getId());

            com.google.firebase.messaging.Notification a = com.google.firebase.messaging.Notification
                    .builder()
                    .setTitle(notification.getTitle())
                    .setBody(notification.getDescription())
                    .build();

            Message message = Message
                    .builder()
                    .setToken(user.getFcmToken())
                    .setNotification(a)
//                    .putAllData(notification.getLink())
                    .build();
            return firebaseMessaging.send(message);

    }

}
