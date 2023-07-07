package com.example.thanhhoa.services.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String convertFileToImageURL(MultipartFile file) throws IOException;

    String addImage(String entityName, String entityID, MultipartFile file) throws IOException;

    String deleteImage(String entityName, String entityID, String imageID) throws IOException;
}
