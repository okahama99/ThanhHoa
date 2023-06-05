package com.example.thanhhoa.services.firebase;

public class FirebaseResource {
    public static final String PROJECT_ID = "thanhhoa-5b7f5";
    public static final String BUCKET_NAME = PROJECT_ID + ".appspot.com";
    public static final String IMAGE_URL =
            "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";
    public static final String PRIVATE_KEY =
            "src/main/java/com/example/thanhhoa/services/firebase/firebase.json";
}