package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ReportModels.CreateReportModel;
import com.example.thanhhoa.dtos.ReportModels.ShowReportModel;
import com.example.thanhhoa.dtos.ReportModels.UpdateReportModel;
import com.example.thanhhoa.services.report.ReportService;
import com.example.thanhhoa.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/getByCustomerToken", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowReportModel> getByCustomerToken(HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        List<ShowReportModel> list = reportService.getByUserID(jwtUtil.getUserIDFromRequest(request));
        return list;
    }

    @GetMapping(value = "/getByContractDetailID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowReportModel> getByContractDetailID(@RequestParam String contractDetailID,
                                                HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        List<ShowReportModel> list = reportService.getByContractDetailID(contractDetailID);
        return list;
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> create(@RequestBody CreateReportModel createReportModel,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = reportService.create(createReportModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> update(@RequestBody UpdateReportModel updateReportModel,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = reportService.update(updateReportModel);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{reportID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> delete(@PathVariable(name = "reportID") String reportID,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = reportService.delete(reportID);
        if(result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}