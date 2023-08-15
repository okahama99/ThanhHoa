package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ServiceTypeModels.CreateServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceTypeModels.ShowServiceTypeModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.serviceType.ServiceTypeService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/serviceType")
public class ServiceTypeController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private ServiceTypeService serviceTypeService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> create(@RequestBody CreateServiceTypeModel createServiceTypeModel,
                                         HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = serviceTypeService.create(createServiceTypeModel);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{serviceTypeID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> delete(@PathVariable(name = "serviceTypeID") String serviceTypeID,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = serviceTypeService.delete(serviceTypeID);
        if(!result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowServiceTypeModel> getAll(@RequestParam(required = false) String serviceID,
                                      @RequestParam int pageNo,
                                      @RequestParam int pageSize,
                                      @RequestParam(required = false, defaultValue = "ID") SearchType.SERVICE_TYPE sortBy,
                                      @RequestParam(required = false, defaultValue = "true") Boolean sortAsc,
                                      HttpServletRequest request) {

        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.equals("APPLYDATE")) {
            paging = util.makePaging(pageNo, pageSize, "applyDate", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(serviceID != null){
            return serviceTypeService.getByServiceID(serviceID, paging);
        }else{
            return serviceTypeService.getAll(paging);
        }

    }

    @GetMapping(value = "/getByID", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowServiceTypeModel getByID(@RequestParam String serviceTypeID) {
        ShowServiceTypeModel model = serviceTypeService.getByID(serviceTypeID);
        return model;
    }
}
