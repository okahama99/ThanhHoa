package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.CreateContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractService {
    List<ShowContractModel> getAllContractByUsername(String username, Pageable pageable);

    List<ShowContractDetailModel> getContractDetailByContractID(String contractID);

    String createContract(CreateContractModel createContractModel, String username);

    String updateContract(UpdateContractModel updateContractModel, String username);
}
