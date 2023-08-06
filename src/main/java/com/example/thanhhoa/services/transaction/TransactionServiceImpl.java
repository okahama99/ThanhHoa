package com.example.thanhhoa.services.transaction;

import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.Transaction;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public String updateTransaction(String transactionID, String orderID, String contractID) {
        Optional<Transaction> checkExisted = transactionRepository.findById(transactionID);
        if(checkExisted == null) {
            return "Không tìm thấy Transaction với ID là " + transactionID + ".";
        }
        Transaction transaction = checkExisted.get();
        if(orderID != null){
            Optional<tblOrder> order = orderRepository.findById(orderID);
            if(order == null) {
                return "Không tìm thấy Order với ID là " + orderID + ".";
            }
            transaction.setTblOrder(order.get());
        }else if(contractID != null){
            Optional<Contract> contract = contractRepository.findById(contractID);
            if(contract == null) {
                return "Không tìm thấy Contract với ID là " + contractID + ".";
            }
            transaction.setContract(contract.get());
        }else{
            return "Không tìm thấy orderID hay contractID.";
        }

        transactionRepository.save(transaction);
        return "Chỉnh sửa thành công.";
    }
}
