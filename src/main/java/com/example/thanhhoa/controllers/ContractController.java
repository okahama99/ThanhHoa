package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.contract.ContractService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private ContractService contractService;

    @GetMapping(value = "/byCustomerToken", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractModel> getAllContractByUsername(@RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ID") SearchType.CONTRACT sortBy,
                                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc,
                                                     HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String authTokenHeader = request.getHeader("Authorization");
        String token = authTokenHeader.substring(7);

        Pageable paging;
        if (sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        }else if (sortBy.equals("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        return contractService.getAllContractByUsername(jwtUtil.getUserNameFromJWT(token), paging);
    }

    @GetMapping(value = "/contractDetail/{contractID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractDetailModel> getContractDetailByContractID(@PathVariable("contractID") String contractID) {
        List<ShowContractDetailModel> model = contractService.getContractDetailByContractID(contractID);
        return model;
    }


}
