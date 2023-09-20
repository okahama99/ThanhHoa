package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ContractModels.ApproveContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateCustomerContractModel;
import com.example.thanhhoa.dtos.ContractModels.CreateManagerContractModel;
import com.example.thanhhoa.dtos.ContractModels.GetStaffModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import com.example.thanhhoa.dtos.ContractModels.UpdateContractDetailModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.services.contract.ContractService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import java.time.LocalDateTime;
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
    ResponseEntity<Object> getAllContractByUserID(@RequestParam(value = "Staff / Customer") String role,
                                                  @RequestParam int pageNo,
                                                  @RequestParam int pageSize,
                                                  @RequestParam SearchType.CONTRACT sortBy,
                                                  @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                                  HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(role.equalsIgnoreCase("staff")) {
            return ResponseEntity.ok().body(contractService.getAllContractByUserID(jwtUtil.getUserIDFromRequest(request), "Staff", paging));
        } else if(role.equalsIgnoreCase("customer")) {
            return ResponseEntity.ok().body(contractService.getAllContractByUserID(jwtUtil.getUserIDFromRequest(request), "Customer", paging));
        } else {
            return ResponseEntity.badRequest().body("Nhập dữ liệu sai, Role phải là Staff hoặc Customer ( Không phân biệt hoa thường ).");
        }
    }

    @GetMapping(value = "/byCustomerTokenAndStatus", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> byCustomerTokenAndStatus(@RequestParam(required = false) String status,
                                                    @RequestParam int pageNo,
                                                    @RequestParam int pageSize,
                                                    @RequestParam SearchType.CONTRACT sortBy,
                                                    @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                                    HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(status != null) {
            return ResponseEntity.ok().body(contractService.getAllContractByUserIDAndStatus(jwtUtil.getUserIDFromRequest(request), jwtUtil.getRoleNameFromRequest(request), Status.valueOf(status), paging));
        } else {
            return ResponseEntity.ok().body(contractService.getAllContractByUserID(jwtUtil.getUserIDFromRequest(request), jwtUtil.getRoleNameFromRequest(request), paging));
        }
    }

    @GetMapping(value = "/v2/getAllContractByStatus", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getAllContractForOwner(@RequestParam SearchType.CONTRACT_TYPE type,
                                                  @RequestParam int pageNo,
                                                  @RequestParam int pageSize,
                                                  @RequestParam SearchType.CONTRACT sortBy,
                                                  @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                                  HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        return ResponseEntity.ok().body(contractService.getAllContractByStatus(type.toString(), paging));
    }

    @GetMapping(value = "/contractDetail/{contractID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractDetailModel> getContractDetailByContractID(@PathVariable("contractID") String contractID,
                                                                @RequestParam int pageNo,
                                                                @RequestParam int pageSize,
                                                                @RequestParam SearchType.CONTRACT_DETAIL sortBy,
                                                                @RequestParam(required = false, defaultValue = "true") boolean sortAsc) {
        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("STARTDATE")) {
            paging = util.makePaging(pageNo, pageSize, "startDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("TIMEWORKING")) {
            paging = util.makePaging(pageNo, pageSize, "timeWorking", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("TOTALPRICE")) {
            paging = util.makePaging(pageNo, pageSize, "totalPrice", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        List<ShowContractDetailModel> model = contractService.getContractDetailByContractID(contractID, paging);
        return model;
    }

    @GetMapping(value = "/getContractDetailByStaffToken", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractDetailModel> getContractDetailByStaffToken(HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        List<ShowContractDetailModel> model = contractService.getAllContractDetailByStaffID(jwtUtil.getUserIDFromRequest(request));
        return model;
    }

    @GetMapping(value = "/v2/getContractDetailByCustomerToken", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractDetailModel> getContractDetailByCustomerToken(@RequestParam(required = false) String timeWorking,
                                                                   @RequestParam int pageNo,
                                                                   @RequestParam int pageSize,
                                                                   @RequestParam SearchType.CONTRACT sortBy,
                                                                   @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(timeWorking != null) {
            return contractService.getAllContractDetailByCustomerIDAndTimeWorking(jwtUtil.getUserIDFromRequest(request), timeWorking, paging);
        } else {
            return contractService.getAllContractDetailByCustomerID(jwtUtil.getUserIDFromRequest(request), paging);
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractModel> getAll(@RequestParam int pageNo,
                                   @RequestParam int pageSize,
                                   @RequestParam SearchType.CONTRACT sortBy,
                                   @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        List<ShowContractModel> model = contractService.getAllContract(paging);
        return model;
    }

    @GetMapping(value = "/getContractDetailByStaffTokenAndDate", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<?> getContractDetailByStaffToken(@RequestParam String from,
                                                    @RequestParam String to,
                                                    HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff") && !roleName.equalsIgnoreCase("Customer")) {
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

        return ResponseEntity.ok().body(contractService.getContractDetailByDateBetween(fromDate, toDate, jwtUtil.getUserIDFromRequest(request), jwtUtil.getRoleNameFromRequest(request)));
    }

    @GetMapping(value = "/getContractDetailByExactDate", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<?> getContractDetailByExactDate(@RequestParam String from,
                                                   @RequestParam String to,
                                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
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

        return ResponseEntity.ok().body(contractService.getContractDetailByExactDate(fromDate, toDate, jwtUtil.getUserIDFromRequest(request)));
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getContractByStoreID(@RequestParam(required = false) String storeID,
                                                @RequestParam(required = false, value = "WAITING / APPROVE / DENIED / STAFFCANCELED / CUSTOMERCANCELED / SIGNED / WORKING / DONE") String status,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam SearchType.CONTRACT sortBy,
                                                @RequestParam(required = false, defaultValue = "true") boolean sortAsc,
                                                HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(storeID == null) {
            return ResponseEntity.badRequest().body("Phải có StoreID để lấy thông tin.");
        } else if(storeID != null && status == null) {
            return ResponseEntity.ok().body(contractService.getContractByStoreID(storeID, paging));
        } else {
            return ResponseEntity.badRequest().body(contractService.getContractByStoreIDAndStatus(storeID, Status.valueOf(status.toUpperCase().trim()), paging));
        }
    }

    @PostMapping(value = "/createContractCustomer", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createContractCustomer(@RequestBody CreateCustomerContractModel createCustomerContractModel,
                                                         HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        String result = contractService.createContractCustomer(createCustomerContractModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping(value = "/createContractManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createContractManager(@RequestBody CreateManagerContractModel createManagerContractModel,
                                                        HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return ResponseEntity.ok().body(contractService.createContractManager(createManagerContractModel));
    }

    @PostMapping(value = "/addContractIMG", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> addContractIMG(@RequestParam String contractID,
                                                 @RequestParam List<String> listURL,
                                                 HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return ResponseEntity.ok().body(contractService.addContractIMG(contractID, listURL));
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateContractDetail(@RequestBody UpdateContractDetailModel updateContractDetailModel, HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = contractService.updateContractDetail(updateContractDetailModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{contractID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteContract(@PathVariable(name = "contractID") String contractID,
                                                 @RequestParam String reason,
                                                 @RequestParam String status,
                                                 HttpServletRequest request) throws FirebaseMessagingException {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = contractService.deleteContract(contractID, reason, Status.valueOf(status));
        if(result.equals("Hủy thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/approveContract", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> approveContract(@RequestBody ApproveContractModel approveContractModel,
                                                  HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        return ResponseEntity.ok().body(contractService.approveContract(approveContractModel));
    }

    @PutMapping(value = "/changeContractStatus", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> changeContractStatus(@RequestParam String contractID,
                                                       @RequestParam(required = false) Long staffID,
                                                       @RequestParam(required = false) String reason,
                                                       @RequestParam String status,
                                                       HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Staff")
                && !roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = contractService.changeContractStatus(contractID, staffID, reason, Status.valueOf(status));
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(value = "/getWaitingContract", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractModel> getWaitingContract(@RequestParam int pageNo,
                                               @RequestParam int pageSize,
                                               @RequestParam(required = false, defaultValue = "ID") SearchType.CONTRACT sortBy,
                                               @RequestParam(required = false, defaultValue = "true") Boolean sortAsc, HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("ENDEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "endedDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        return contractService.getWaitingContract(paging);
    }

    @GetMapping(value = "/getStaffForContract", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<GetStaffModel> getStaffForContract() {
        return contractService.getStaffForContract();
    }

    @GetMapping(value = "/checkStartDateEndDate", produces = "application/json;charset=UTF-8")
    public void checkStartDateEndDate() throws FirebaseMessagingException {
        contractService.checkStartDateEndDate();
    }

    @GetMapping(value = "/getByID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowContractModel getByID(@RequestParam String contractID) {
        ShowContractModel model = contractService.getByID(contractID);
        return model;
    }

    @GetMapping(value = "/v2/getByContractDetailID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowContractModel getByContractDetailID(@RequestParam String contractDetailID) {
        ShowContractModel model = contractService.getByContractDetailID(contractDetailID);
        return model;
    }
}
