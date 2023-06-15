package com.example.thanhhoa.services.firebase;


import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CRUDUserFireBaseService {


    public static String createUser(UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        CollectionReference users = dbFirestore.collection("Users");
        futures.add(users.document(userFCMToken.getAccountID().toString()).set(userFCMToken)
        );
        return "Đã tạo";
    }

    public static String updateUser(UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Users").document(userFCMToken.getAccountID().toString()).set(userFCMToken);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public static String deleteUser(UserFCMToken userFCMToken) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("Users").document(userFCMToken.getAccountID().toString()).delete();
        return "Đã xóa.";
    }

}
