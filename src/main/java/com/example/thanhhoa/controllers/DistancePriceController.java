package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.DistancePriceModels.ShowDistancePriceModel;
import com.example.thanhhoa.services.distancePrice.DistancePriceService;
import com.example.thanhhoa.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/distancePrice")
public class DistancePriceController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DistancePriceService distancePriceService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> add(@RequestParam Double pricePerKm,
                                      HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = distancePriceService.create(pricePerKm);
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ShowDistancePriceModel getServiceTypeByServiceID() {
        return distancePriceService.getNewestDistancePrice();
    }
}
