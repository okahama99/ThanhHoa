package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractService {
    List<ShowContractModel> getAllContractByUsername(String username, Pageable pageable);

    List<ShowContractDetailModel> getContractDetailByContractID(String contractID);
}
