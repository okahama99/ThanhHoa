package com.example.thanhhoa.services.transaction;

import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.ShowStaffModel;
import com.example.thanhhoa.dtos.TransactionModels.CreateTransactionModel;
import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.Transaction;
import com.example.thanhhoa.entities.tblAccount;
import com.example.thanhhoa.entities.tblOrder;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.TransactionRepository;
import com.example.thanhhoa.repositories.UserRepository;
import com.example.thanhhoa.repositories.pagings.TransactionPagingRepository;
import com.example.thanhhoa.services.vnpay.Config;
import com.example.thanhhoa.utils.Util;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionPagingRepository transactionPagingRepository;
    @Autowired
    private Util util;

    @Override
    public String create(CreateTransactionModel createTransactionModel) throws FirebaseMessagingException {
        Transaction transaction = new Transaction();

        // order
        if(createTransactionModel.getOrderID() != null){
            Optional<tblOrder> order = orderRepository.findById(createTransactionModel.getOrderID());
            if(order == null){
                return "Không tồn tại Order với ID là " + createTransactionModel.getOrderID() + ".";
            }
            transaction.setTblOrder(order.get());
        }

        // transaction
        if(createTransactionModel.getContractID() != null){
            Optional<Contract> contract = contractRepository.findById(createTransactionModel.getContractID());
            if(contract == null){
                return "Không tồn tại Contract với ID là " + createTransactionModel.getContractID() + ".";
            }
            transaction.setContract(contract.get());
        }

        // customer
        if(createTransactionModel.getUserID() != null){
            Optional<tblAccount> customer = userRepository.findById(createTransactionModel.getUserID());
            if(customer == null){
                return "Không tồn tại Account với ID là " + createTransactionModel.getUserID() + ".";
            }
            transaction.setUser(customer.get());
        }


        String id = Config.getRandomNumber(8);
        if(transactionRepository.findByTransNo(id) != null){
            id = Config.getRandomNumber(8);
        }

        transaction.setId(id);
        transaction.setAmount(createTransactionModel.getAmount());
        transaction.setCurrency(createTransactionModel.getCurrency());
        transaction.setReason(createTransactionModel.getReason());
        transaction.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        transaction.setStatus(Status.SUCCESS);
        transactionRepository.save(transaction);

        if(transaction.getTblOrder() != null && transaction.getUser() != null){
            util.createNotification("ORDER", transaction.getUser(), transaction.getId(), "tạo");
            util.createNotification("ORDER", transaction.getTblOrder().getStaff(), transaction.getId(), "tạo");
        }
        if(transaction.getContract() != null && transaction.getUser() != null){
            util.createNotification("CONTRACT", transaction.getUser(), transaction.getId(), "tạo");
        }
        return "Tạo thành công.";
    }

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
        model.setBankAccount(transaction.getBankAccount());
        model.setRefundBankCode(transaction.getRefundBankCode());
        model.setBankAccountNo(transaction.getBankAccountNo());

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
        orderModel.setIsRefund(order.getIsRefund());
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
        contractModel.setExpectedEndedDate(contract.getExpectedEndedDate());
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
            model.setBankAccount(transaction.getBankAccount());
            model.setRefundBankCode(transaction.getRefundBankCode());
            model.setBankAccountNo(transaction.getBankAccountNo());

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
            orderModel.setIsRefund(order.getIsRefund());
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
            contractModel.setExpectedEndedDate(contract.getExpectedEndedDate());
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

    @Override
    public List<ShowTransactionModel> getAllStoreContract(String storeID, Pageable pageable) {
        Page<Transaction> pagingResult = transactionPagingRepository.findByContract_Store_Id(storeID, pageable);
        return util.transactionPagingConverter(pagingResult, pageable);
    }

    @Override
    public List<ShowTransactionModel> getAllStoreOrder(String storeID, Pageable pageable) {
        Page<Transaction> pagingResult = transactionPagingRepository.findByTblOrder_Store_Id(storeID, pageable);
        return util.transactionPagingConverter(pagingResult, pageable);
    }
}
