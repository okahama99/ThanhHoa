package com.example.thanhhoa.dtos.ContractModels;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateCustomerContractModel implements Serializable {
    private String title;
    private String fullName;
    private String phone;
    private String address;
    private String storeID;
    private List<CreateContractDetailModel> detailModelList;

    @Nullable
    private String email;
}
