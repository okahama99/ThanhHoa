package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.example.thanhhoa.services.transaction.TransactionService;
import com.example.thanhhoa.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TransactionService transactionService;

    @PutMapping(value = "/updateTransactionFK", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateTransactionFK(@RequestParam String transactionID,
                                                 @RequestParam(required = false) String orderID,
                                                 @RequestParam(required = false) String contractID) {
        String result = transactionService.updateTransaction(transactionID, orderID, contractID);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping(value = "/getByID", produces = "application/json;charset=UTF-8")
    public ShowTransactionModel getByID(@RequestParam String transactionID) {
        return transactionService.getById(transactionID);
    }

    @GetMapping(value = "/getByToken", produces = "application/json;charset=UTF-8")
    public List<ShowTransactionModel> getByToken(HttpServletRequest request) {
        return transactionService.getAllByUserID(jwtUtil.getUserIDFromRequest(request));
    }
}
