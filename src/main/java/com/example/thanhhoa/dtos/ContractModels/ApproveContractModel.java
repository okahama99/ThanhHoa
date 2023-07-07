package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApproveContractModel implements Serializable {
    private String contractID;
    private Double deposit;
    private String paymentMethod;
    private Long staffID;
    private String paymentTypeID;
    private List<String> imageUrlList;
}
