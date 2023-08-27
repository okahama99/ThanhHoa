package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.TransactionModels.ShowTransactionModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.transaction.TransactionService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private Util util;

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

    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowTransactionModel> getAll(@RequestParam(required = false, defaultValue = "false") Boolean byOrder,
                                      @RequestParam(required = false, defaultValue = "false") Boolean byContract,
                                      @RequestParam String storeID,
                                      @RequestParam int pageNo,
                                      @RequestParam int pageSize,
                                      @RequestParam(required = false, defaultValue = "ID") SearchType.TRANSACTION sortBy,
                                      @RequestParam(required = false, defaultValue = "true") Boolean sortAsc, HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.equals("BANKCODE")) {
            paging = util.makePaging(pageNo, pageSize, "bankCode", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(byOrder){
            return transactionService.getAllStoreOrder(storeID, paging);
        }else if(byContract){
            return transactionService.getAllStoreContract(storeID, paging);
        }else{
            return null;
        }
    }
}
