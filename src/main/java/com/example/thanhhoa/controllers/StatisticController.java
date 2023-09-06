package com.example.thanhhoa.controllers;

import com.example.thanhhoa.services.statistic.StatisticService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<?> getStatistic(@RequestParam(required = false) String storeID,
                                   @RequestParam String from,
                                   @RequestParam String to,
                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        LocalDateTime fromDate = util.isLocalDateTimeValid(from);
        LocalDateTime toDate = util.isLocalDateTimeValid(to);
        if(fromDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }
        if(toDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }

        return ResponseEntity.ok().body(statisticService.getStatistic(storeID, fromDate, toDate));

    }

    @GetMapping(value = "/getStatisticWithAllStore", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<?> getStatistic(@RequestParam String from,
                                   @RequestParam String to,
                                   HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner") && !roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        LocalDateTime fromDate = util.isLocalDateTimeValid(from);
        LocalDateTime toDate = util.isLocalDateTimeValid(to);
        if(fromDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }
        if(toDate == null) {
            return ResponseEntity.badRequest().body("Nhập theo khuôn được định sẵn yyyy-MM-dd, ví dụ : 2021-12-21");
        }

        return ResponseEntity.ok().body(statisticService.getStatisticOfAllStore(fromDate, toDate));

    }
}
