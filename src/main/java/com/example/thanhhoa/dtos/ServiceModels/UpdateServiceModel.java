package com.example.thanhhoa.dtos.ServiceModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateServiceModel implements Serializable {
    private String serviceID;
    private String name;
    private Double price;
    private String description;
    private List<String> typeIDList;
    @Nullable
    private List<String> listURL;
}
