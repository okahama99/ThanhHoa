package com.example.thanhhoa.services.vnpay;

import com.example.thanhhoa.entities.Transaction;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.TransactionRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private Util util;

    @PostMapping(value = "/submitOrder", produces = "application/json;charset=UTF-8")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("reason") String reason,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(orderTotal, reason, baseUrl, request.getRemoteAddr());
    }

    @GetMapping("/getPaymentInfo")
    public ResponseEntity<?> getPaymentInfo(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        Transaction transaction = new Transaction();
        Transaction lastTransaction = transactionRepository.findFirstByOrderByIdDesc();
        if(lastTransaction == null) {
            transaction.setId(util.createNewID("T"));
        } else {
            transaction.setId(util.createIDFromLastID("T", 1, lastTransaction.getId()));
        }
        transaction.setBillNo(request.getParameter("vnp_TxnRef"));
        transaction.setTransNo(request.getParameter("vnp_TransactionNo"));
        transaction.setBankCode(request.getParameter("vnp_BankCode"));
        transaction.setCardType(request.getParameter("vnp_CardType"));
        transaction.setAmount(Integer.parseInt(request.getParameter("vnp_Amount")));
        transaction.setCurrency("VND");
        transaction.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        transaction.setReason(request.getParameter("vnp_OrderInfo"));
        if(paymentStatus == 1) {
            transaction.setStatus(Status.SUCCESS);
            transactionRepository.save(transaction);
            return ResponseEntity.ok().body(transaction);
        } else if(paymentStatus == 0) {
            transaction.setStatus(Status.FAIL);
            transactionRepository.save(transaction);
            return ResponseEntity.badRequest().body(transaction);
        } else {
            return ResponseEntity.badRequest().body("Mã Secure Hash không hợp lệ.");
        }
    }
}
