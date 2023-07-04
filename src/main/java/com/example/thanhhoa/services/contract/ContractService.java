package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractService {
    List<ShowContractModel> getAllContractByUserID(Long userID);

    List<ShowContractDetailModel> getContractDetailByContractID(String contractID);

    String createContract(CreateContractModel createContractModel, Long userID);

    String updateContract(UpdateContractModel updateContractModel, Long userID);

    String deleteContract(String contractID, String reason, Status status);

    String approveContract(ApproveContractModel approveContractModel);

    String changeContractStatus(String contractID, Status status);

    String addWorkingDate(String contractDetailID);

    List<ShowContractModel> getWaitingContract(Pageable pageable);

    List<GetStaffModel> getStaffForContract();
}
