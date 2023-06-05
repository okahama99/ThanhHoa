package com.example.thanhhoa.services.firebaseIMG;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class FirebaseImageServiceImpl implements ImageService {

//    @Autowired
//    Properties properties;

//    @EventListener
//    public void init(ApplicationReadyEvent event) {
//
//        // initialize Firebase
//
//        try {
//
//            ClassPathResource serviceAccount = new ClassPathResource("com/example/thanhhoa/services/firebase/firebase.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//                    .setStorageBucket(properties.getBucketName())
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//
//        }
//    }

//    @Override
//    public String getImageUrl(String name) {
//        return String.format(properties.imageUrl, name);
//    }

    @Override
    public String save(MultipartFile file) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {

        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }

//    @Data
//    @Configuration
//    @ConfigurationProperties(prefix = "firebase")
//    public class Properties {
//
//        private String bucketName;
//
//        private String imageUrl;
//    }

}
