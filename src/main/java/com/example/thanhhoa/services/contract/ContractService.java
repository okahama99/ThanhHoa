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
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ContractService {

    List<ShowContractModel> getAllContractByUserID(Long userID,String role, Pageable pageable);

    List<ShowContractModel> getAllContractByUserIDAndStatus(Long userID,String role, Status status, Pageable pageable);

    List<ShowContractModel> getAllContract(Pageable pageable);

    List<ShowContractDetailModel> getAllContractDetailByUserID(Long userID);

    List<ShowContractDetailModel> getContractDetailByContractID(String contractID, Pageable pageable);

    String createContractCustomer(CreateCustomerContractModel createCustomerContractModel, Long userID) throws FirebaseMessagingException;

    String createContractManager(CreateManagerContractModel createManagerContractModel) throws IOException, FirebaseMessagingException;

    String updateContract(UpdateContractModel updateContractModel, Long userID);

    String deleteContract(String contractID, String reason, Status status) throws FirebaseMessagingException;

    String approveContract(ApproveContractModel approveContractModel) throws IOException, FirebaseMessagingException;

    String changeContractStatus(String contractID, Status status) throws FirebaseMessagingException;

    String addContractIMG(String contractID, Double deposit, String paymentTypeID, List<String> listURL);

    List<ShowContractModel> getWaitingContract(Pageable pageable);

    List<GetStaffModel> getStaffForContract();

    List<ShowContractModel> getContractByStoreID(String storeID, Pageable pageable);

    List<ShowContractModel> getContractByStoreIDAndStatus(String storeID, Status status, Pageable pageable);

    List<ShowPaymentTypeModel> getPaymentType();

    List<ShowContractDetailModel> getContractDetailByDateBetween(LocalDateTime from, LocalDateTime to, Long staffID, String role);

    List<ShowContractDetailModel> getContractDetailByExactDate(LocalDateTime from, LocalDateTime to, Long staffID);


}
