package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.ServiceModels.ShowServiceModel;
import com.example.thanhhoa.dtos.ServiceModels.ShowServiceTypeModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.services.service.ServiceService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
