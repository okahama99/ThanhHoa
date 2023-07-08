package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.entities.WorkingDate;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.workingDate.WorkingDateService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/workingDate")
public class WorkingDateController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private WorkingDateService workingDateService;

    @PostMapping(value = "/addWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addWorkingDate(@RequestParam String contractDetailID,
                                                 HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.addWorkingDate(contractDetailID);
        if(result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(value = "/getWorkingDate/{contractDetailID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowWorkingDateModel> getByContractDetailID(@PathVariable(name = "contractDetailID") String contractDetailID,
                                                     @RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ID") SearchType.WORKING_DATE sortBy,
                                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        if(sortBy.equals("WORKINGDATE")) {
            paging = util.makePaging(pageNo, pageSize, "workingDate", sortAsc);
        }else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        return workingDateService.getAllByContractDetailID(contractDetailID, paging);
    }

    @GetMapping(value = "/getWorkingDate/{workingDateID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getByID(@PathVariable("workingDateID") String workingDateID) {
        WorkingDate result = workingDateService.getByID(workingDateID);
        if(result == null){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok().body(result);
    }
}
