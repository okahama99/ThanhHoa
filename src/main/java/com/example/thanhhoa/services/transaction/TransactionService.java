package com.example.thanhhoa.services.transaction;

import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;

import java.util.List;

public interface TransactionService {

    String updateTransaction(String transactionID, String orderID, String contractID);

    ShowTransactionModel getById(String transactionID);

    List<ShowTransactionModel> getAllByUserID(Long userID);
}
