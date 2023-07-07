package com.example.thanhhoa.controllers;

import com.example.thanhhoa.services.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/convertFromFileToImageURL", produces = "application/json;charset=UTF-8", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public String convertFromFileToImageURL(@RequestPart(name = "file") MultipartFile file) throws Exception {
        return imageService.convertFileToImageURL(file);
    }

    @PutMapping(value = "/addImage", produces = "application/json;charset=UTF-8", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addImage(@RequestParam String entityName,
                           @RequestParam String entityID,
                           @RequestPart(name = "file") MultipartFile file) throws Exception {
        return imageService.addImage(entityName, entityID, file);
    }

    @DeleteMapping(value = "/deleteImage", produces = "application/json;charset=UTF-8")
    public String deleteImage(@RequestParam String entityName,
                              @RequestParam String entityID,
                              @RequestParam String imageID) throws Exception {
        return imageService.deleteImage(entityName, entityID, imageID);
    }
}
