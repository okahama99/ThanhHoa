package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.WorkingDateModels.ShowWorkingDateModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.workingDate.WorkingDateService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    @PostMapping(value = "/v2/addStartWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addStartWorkingDate(@RequestParam String workingDateID,
                                                      @RequestParam String startWorkingIMG,
                                                      @RequestParam Long staffID,
                                                      HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.addStartWorkingDate(workingDateID, startWorkingIMG, staffID);
        if(result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value = "/v2/compensateWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> compensateWorkingDate(@RequestParam String workingDateID,
                                                        @RequestParam String date,
                                                        HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.compensateWorkingDate(workingDateID, date);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value = "/v2/addEndWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addEndWorkingDate(@RequestParam String workingDateID,
                                                    @RequestParam String endWorkingIMG,
                                                    @RequestParam Long staffID,
                                                    HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.addEndWorkingDate(workingDateID, endWorkingIMG, staffID);
        if(result.equals("Thêm thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping(value = "/v2/updateWorkingDateStaffID", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateWorkingDateStaffID(@RequestParam String workingDateID,
                                                           @RequestParam Long staffID,
                                                           @RequestParam String note,
                                                           HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.updateWorkingDateStaffID(workingDateID, note, staffID);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping(value = "/v2/swapWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> swapWorkingDate(@RequestParam String workingDateID,
                                                  @RequestParam String date,
                                                  HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = workingDateService.swapWorkingDate(workingDateID, date);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(value = "/getWorkingDateByContractDetailID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowWorkingDateModel> getByContractDetailID(@RequestParam String contractDetailID,
                                                     @RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ID") SearchType.WORKING_DATE sortBy,
                                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("WORKINGDATE")) {
            paging = util.makePaging(pageNo, pageSize, "workingDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        return workingDateService.getAllByContractDetailID(contractDetailID, paging);
    }

    @GetMapping(value = "/v2/getAllByContractID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowWorkingDateModel> getAllByContractID(@RequestParam String contractID,
                                                  @RequestParam int pageNo,
                                                  @RequestParam int pageSize,
                                                  @RequestParam(required = false, defaultValue = "ID") SearchType.WORKING_DATE sortBy,
                                                  @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("WORKINGDATE")) {
            paging = util.makePaging(pageNo, pageSize, "workingDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        return workingDateService.getAllByContractID(contractID, paging);
    }

    @GetMapping(value = "/getWorkingDateByID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getByID(@RequestParam String workingDateID) {
        ShowWorkingDateModel result = workingDateService.getByID(workingDateID);
        if(result == null) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/getByStaffToken", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByStaffToken(HttpServletRequest request) {
        return ResponseEntity.ok().body(workingDateService.getWorkingDateByStaffID(jwtUtil.getUserIDFromRequest(request)));
    }

    @GetMapping(value = "/v2/getByStaffID", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByStaffID(@RequestParam Long userID,
                                               @RequestParam String from,
                                               @RequestParam String to,
                                               HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Manager")
                && !roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        LocalDateTime fromDate = util.isLocalDateTimeValid(from);
        if(fromDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }
        LocalDateTime toDate = util.isLocalDateTimeValid(to);
        if(toDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }

        return ResponseEntity.ok().body(workingDateService.getByStaffID(userID, fromDate, toDate));
    }

    @GetMapping(value = "/v2/getByWorkingDate", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByWorkingDate(@RequestParam(required = false) String contractDetailID,
                                                   @RequestParam String from,
                                                   @RequestParam String to,
                                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Manager")
                && !roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        LocalDateTime fromDate = util.isLocalDateTimeValid(from);
        if(fromDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }
        LocalDateTime toDate = util.isLocalDateTimeValid(to);
        if(toDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }

        if(contractDetailID != null) {
            return ResponseEntity.ok().body(workingDateService.getByWorkingDate(contractDetailID, fromDate, toDate));
        } else {
            if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
                return ResponseEntity.badRequest().body("Token không phải CUSTOMER/STAFF, vui lòng nhập ContractDetailID");
            } else {
                return ResponseEntity.ok().body(workingDateService.getByWorkingDateInRange(jwtUtil.getUserIDFromRequest(request), fromDate, toDate, roleName));
            }
        }
    }

    @PostMapping(value = "/v2/generateSchedule", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> generateSchedule(@RequestParam String contractDetailID,
                                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return ResponseEntity.ok().body(workingDateService.generateWorkingSchedule(contractDetailID));
    }

    @GetMapping(value = "/v2/checkMissedDate", produces = "application/json;charset=UTF-8")
    public void checkWorkingDate() {
        workingDateService.checkWorkingDate();
    }
}
