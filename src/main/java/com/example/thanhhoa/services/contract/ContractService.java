package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateCustomerContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateManagerContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.ShowPaymentTypeModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractModel;
import com.example.thanhhoa.enums.Status;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ContractService {

    List<ShowContractModel> getAllContractByUserID(Long userID,String role, Pageable pageable);

    List<ShowContractDetailModel> getAllContractDetailByUserID(Long userID);

    List<ShowContractDetailModel> getContractDetailByContractID(String contractID, Pageable pageable);

    String createContractCustomer(CreateCustomerContractModel createCustomerContractModel, Long userID);

    String createContractManager(CreateManagerContractModel createManagerContractModel) throws IOException;

    String updateContract(UpdateContractModel updateContractModel, Long userID);

    String deleteContract(String contractID, String reason, Status status);

    String approveContract(ApproveContractModel approveContractModel) throws IOException;

    String changeContractStatus(String contractID, Status status);

    List<ShowContractModel> getWaitingContract(Pageable pageable);

    List<GetStaffModel> getStaffForContract();

    List<ShowContractModel> getContractByStoreID(String storeID, Pageable pageable);

    List<ShowContractModel> getContractByStoreIDAndStatus(String storeID, Status status, Pageable pageable);

    List<ShowPaymentTypeModel> getPaymentType();
}
