package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ServiceModels.CreateServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.dtos.ServiceModels.UpdateServiceModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.service.ServiceService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
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
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private ServiceService serviceService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createService(@RequestBody CreateServiceModel createServiceModel,
                                                HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = serviceService.createService(createServiceModel);
        if (result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateService(@RequestBody UpdateServiceModel updateServiceModel,
                                              HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner") || !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = serviceService.updateService(updateServiceModel);
        if (result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/{serviceID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteService(@PathVariable(name = "serviceID") String serviceID,
                                              HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if (!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        if (serviceService.deleteService(serviceID)) {
            return ResponseEntity.ok().body("Xóa dịch vụ thành công.");
        } else {
            return ResponseEntity.badRequest().body("Xóa dịch vụ thất bại.");
        }
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowServiceModel> getAllService(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam(required = false, defaultValue = "ID") SearchType.SERVICE sortBy,
                                         @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        return serviceService.getAllService(util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/serviceType/{serviceID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowServiceTypeModel> getServiceTypeByServiceID(@PathVariable("serviceID") String serviceID) {
        List<ShowServiceTypeModel> model = serviceService.getServiceTypeByServiceID(serviceID);
        return model;
    }
}
