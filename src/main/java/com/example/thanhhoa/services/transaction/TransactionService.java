package com.example.thanhhoa.services.transaction;

import com.example.thanhhoa.dtos.TransactionModels.CreateTransactionModel;
import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    String create(CreateTransactionModel createTransactionModel) throws FirebaseMessagingException;

    String updateTransaction(String transactionID, String orderID, String contractID);

    ShowTransactionModel getById(String transactionID);

    List<ShowTransactionModel> getAllByUserID(Long userID);

    List<ShowTransactionModel> getAllStoreContract(String storeID, Pageable pageable);

    List<ShowTransactionModel> getAllStoreOrder(String storeID, Pageable pageable);


}
