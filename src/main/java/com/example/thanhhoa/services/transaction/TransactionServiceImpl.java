package com.example.thanhhoa.services.transaction;

import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.Transaction;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

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
        if(orderID != null) {
            Optional<tblOrder> order = orderRepository.findById(orderID);
            if(order == null) {
                return "Không tìm thấy Order với ID là " + orderID + ".";
            }
            transaction.setTblOrder(order.get());
            order.get().setIsPaid(true);
            orderRepository.save(order.get());
        } else if(contractID != null) {
            Optional<Contract> contract = contractRepository.findById(contractID);
            if(contract == null) {
                return "Không tìm thấy Contract với ID là " + contractID + ".";
            }
            transaction.setContract(contract.get());
        } else {
            return "Không tìm thấy orderID hay contractID.";
        }

        transactionRepository.save(transaction);
        return "Chỉnh sửa thành công.";
    }

    @Override
    public ShowTransactionModel getById(String transactionID) {
        Optional<Transaction> checkExisted = transactionRepository.findById(transactionID);
        if(checkExisted == null) {
            return null;
        }
        Transaction transaction = checkExisted.get();
        ShowTransactionModel model = new ShowTransactionModel();
        model.setId(transaction.getId());
        model.setAmount(transaction.getAmount());
        model.setBankCode(transaction.getBankCode());
        model.setBillNo(transaction.getBillNo());
        model.setCardType(transaction.getCardType());
        model.setCreatedDate(transaction.getCreateDate());
        model.setCurrency(transaction.getCurrency());
        model.setReason(transaction.getReason());
        model.setTransNo(transaction.getTransNo());
        model.setStatus(transaction.getStatus());

        // order
        tblOrder order = transaction.getTblOrder();
        ShowOrderModel orderModel = new ShowOrderModel();
        orderModel.setId(order.getId());
        orderModel.setFullName(order.getFullName());
        orderModel.setAddress(order.getAddress());
        orderModel.setEmail(order.getEmail());
        orderModel.setPhone(order.getPhone());
        orderModel.setCreatedDate(order.getCreatedDate());
        orderModel.setPackageDate(order.getPackageDate());
        orderModel.setDeliveryDate(order.getDeliveryDate());
        orderModel.setReceivedDate(order.getReceivedDate());
        orderModel.setApproveDate(order.getApproveDate());
        orderModel.setRejectDate(order.getRejectDate());
        orderModel.setPaymentMethod(order.getPaymentMethod());
        orderModel.setProgressStatus(order.getProgressStatus());
        orderModel.setReason(order.getReason());
        orderModel.setLatLong(order.getLatLong());
        orderModel.setDistance(order.getDistance());
        orderModel.setTotalShipCost(order.getTotalShipCost());
        orderModel.setTotal(order.getTotal());
        orderModel.setIsPaid(order.getIsPaid());
        orderModel.setReceiptIMG(order.getReceiptIMG());

        //contract
        Contract contract = transaction.getContract();
        ShowContractModel contractModel = new ShowContractModel();
        contractModel.setId(contract.getId());
        contractModel.setFullName(contract.getFullName());
        contractModel.setAddress(contract.getAddress());
        contractModel.setEmail(contract.getEmail());
        contractModel.setPhone(contract.getPhone());
        contractModel.setTitle(contract.getTitle());
        contractModel.setPaymentMethod(contract.getPaymentMethod());
        contractModel.setReason(contract.getReason());
        contractModel.setCreatedDate(contract.getCreatedDate());
        contractModel.setApprovedDate(contract.getApprovedDate());
        contractModel.setRejectedDate(contract.getRejectedDate());
        contractModel.setStartedDate(contract.getStartedDate());
        contractModel.setEndedDate(contract.getEndedDate());
        contractModel.setTotal(contract.getTotal());
        contractModel.setIsFeedback(contract.getIsFeedback());
        contractModel.setIsSigned(contract.getIsSigned());
        contractModel.setIsPaid(contract.getIsPaid());

        // user
        tblAccount user = transaction.getUser();
        ShowStaffModel userModel = new ShowStaffModel();
        userModel.setId(user.getId());
        userModel.setAddress(user.getAddress());
        userModel.setEmail(user.getEmail());
        userModel.setPhone(user.getPhone());
        userModel.setFullName(user.getFullName());
        userModel.setAvatar(user.getAvatar());

        model.setShowOrderModel(orderModel);
        model.setShowContractModel(contractModel);
        model.setShowStaffModel(userModel);
        return model;
    }

    @Override
    public List<ShowTransactionModel> getAllByUserID(Long userID) {
        List<Transaction> transactionList = transactionRepository.findAllByUser_Id(userID);
        if(transactionList == null){
            return null;
        }
        List<ShowTransactionModel> modelList = new ArrayList<>();
        for(Transaction transaction : transactionList) {
            ShowTransactionModel model = new ShowTransactionModel();
            model.setId(transaction.getId());
            model.setAmount(transaction.getAmount());
            model.setBankCode(transaction.getBankCode());
            model.setBillNo(transaction.getBillNo());
            model.setCardType(transaction.getCardType());
            model.setCreatedDate(transaction.getCreateDate());
            model.setCurrency(transaction.getCurrency());
            model.setReason(transaction.getReason());
            model.setTransNo(transaction.getTransNo());
            model.setStatus(transaction.getStatus());

            // order
            tblOrder order = transaction.getTblOrder();
            ShowOrderModel orderModel = new ShowOrderModel();
            orderModel.setId(order.getId());
            orderModel.setFullName(order.getFullName());
            orderModel.setAddress(order.getAddress());
            orderModel.setEmail(order.getEmail());
            orderModel.setPhone(order.getPhone());
            orderModel.setCreatedDate(order.getCreatedDate());
            orderModel.setPackageDate(order.getPackageDate());
            orderModel.setDeliveryDate(order.getDeliveryDate());
            orderModel.setReceivedDate(order.getReceivedDate());
            orderModel.setApproveDate(order.getApproveDate());
            orderModel.setRejectDate(order.getRejectDate());
            orderModel.setPaymentMethod(order.getPaymentMethod());
            orderModel.setProgressStatus(order.getProgressStatus());
            orderModel.setReason(order.getReason());
            orderModel.setLatLong(order.getLatLong());
            orderModel.setDistance(order.getDistance());
            orderModel.setTotalShipCost(order.getTotalShipCost());
            orderModel.setTotal(order.getTotal());
            orderModel.setIsPaid(order.getIsPaid());
            orderModel.setReceiptIMG(order.getReceiptIMG());

            //contract
            Contract contract = transaction.getContract();
            ShowContractModel contractModel = new ShowContractModel();
            contractModel.setId(contract.getId());
            contractModel.setFullName(contract.getFullName());
            contractModel.setAddress(contract.getAddress());
            contractModel.setEmail(contract.getEmail());
            contractModel.setPhone(contract.getPhone());
            contractModel.setTitle(contract.getTitle());
            contractModel.setPaymentMethod(contract.getPaymentMethod());
            contractModel.setReason(contract.getReason());
            contractModel.setCreatedDate(contract.getCreatedDate());
            contractModel.setApprovedDate(contract.getApprovedDate());
            contractModel.setRejectedDate(contract.getRejectedDate());
            contractModel.setStartedDate(contract.getStartedDate());
            contractModel.setEndedDate(contract.getEndedDate());
            contractModel.setTotal(contract.getTotal());
            contractModel.setIsFeedback(contract.getIsFeedback());
            contractModel.setIsSigned(contract.getIsSigned());
            contractModel.setIsPaid(contract.getIsPaid());

            // user
            tblAccount user = transaction.getUser();
            ShowStaffModel userModel = new ShowStaffModel();
            userModel.setId(user.getId());
            userModel.setAddress(user.getAddress());
            userModel.setEmail(user.getEmail());
            userModel.setPhone(user.getPhone());
            userModel.setFullName(user.getFullName());
            userModel.setAvatar(user.getAvatar());

            model.setShowOrderModel(orderModel);
            model.setShowContractModel(contractModel);
            model.setShowStaffModel(userModel);

            modelList.add(model);
        }
        return modelList;
    }
}
