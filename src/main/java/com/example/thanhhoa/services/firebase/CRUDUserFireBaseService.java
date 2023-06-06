package com.example.thanhhoa.services.firebase;


import com.example.thanhhoa.dtos.UserModels.UserFCMToken;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class CRUDUserFireBaseService {


    public static String saveUser(UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Users").document(userFCMToken.getAccountID().toString()).set(userFCMToken);
        return collectionsApiFuture.get().getUpdateTime().toString();
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
