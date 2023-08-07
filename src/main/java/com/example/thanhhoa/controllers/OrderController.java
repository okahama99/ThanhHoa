package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.OrderModels.CreateOrderModel;
import com.example.thanhhoa.dtos.OrderModels.GetStaffModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderDetailModel;
import com.example.thanhhoa.dtos.OrderModels.ShowOrderModel;
import com.example.thanhhoa.dtos.OrderModels.UpdateOrderModel;
import com.example.thanhhoa.enums.SearchType;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.services.order.OrderService;
import com.example.thanhhoa.utils.JwtUtil;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private Util util;
    @Autowired
    private OrderService orderService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderModel createOrderModel, HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = orderService.createOrder(createOrderModel, jwtUtil.getUserIDFromRequest(request));
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateOrder(@RequestBody UpdateOrderModel updateOrderModel, HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = orderService.updateOrder(updateOrderModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/rejectOrder", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteOrder(@RequestParam String orderID,
                                              @RequestParam String reason,
                                              @RequestParam String status,
                                              HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = orderService.deleteOrder(orderID, reason, Status.valueOf(status));
        if(result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/approveOrder/{orderID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> approveOrder(@PathVariable(name = "orderID") String orderID,
                                               @RequestParam Long staffID,
                                               HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = orderService.approveOrder(orderID, staffID);
        if(result.equals("Chấp nhận thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/changeOrderStatus", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> changeOrderStatus(@RequestParam String orderID,
                                                    @RequestParam(required = false) String receiptIMG,
                                                    @RequestParam SearchType.ORDER_STATUS status,
                                                    HttpServletRequest request) throws Exception {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        if(orderService.changeOrderStatus(orderID, receiptIMG, status.toString())) {
            return ResponseEntity.ok().body("Thay đổi thành công.");
        } else {
            return ResponseEntity.badRequest().body("Thay đổi thất bại.");
        }
    }

    @GetMapping(value = "/getAllOrderByUsername", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderModel> getAllOrderByUsername(@RequestParam(required = false) String status,
                                               @RequestParam(required = false) String storeID,
                                               @RequestParam int pageNo,
                                               @RequestParam int pageSize,
                                               @RequestParam(defaultValue = "ID") SearchType.ORDER sortBy,
                                               @RequestParam(defaultValue = "true") Boolean sortAsc,
                                               HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")
                && !roleName.equalsIgnoreCase("Manager") && !roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("RECEIVEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "receivedDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("PROGRESSSTATUS")) {
            paging = util.makePaging(pageNo, pageSize, "progressStatus", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        if(status != null) {
            return orderService.getAllByStatusOrderByUserID(Status.valueOf(status), jwtUtil.getUserIDFromRequest(request), jwtUtil.getRoleNameFromRequest(request), paging);
        } else if(storeID != null) {
            return orderService.getAllOrderByStoreID(storeID, paging);
        } else {
            return orderService.getAllOrderByUserID(jwtUtil.getUserIDFromRequest(request), jwtUtil.getRoleNameFromRequest(request), paging);
        }

    }

    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderModel> getAll(@RequestParam int pageNo,
                                @RequestParam int pageSize,
                                @RequestParam(defaultValue = "ID") SearchType.ORDER sortBy,
                                @RequestParam(defaultValue = "true") Boolean sortAsc,
                                HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Owner")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.toString().equalsIgnoreCase("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("RECEIVEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "receivedDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("PROGRESSSTATUS")) {
            paging = util.makePaging(pageNo, pageSize, "progressStatus", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }
        return orderService.getAllOrder(paging);
    }

    @GetMapping(value = "/getWaitingOrder", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderModel> getWaitingOrder(@RequestParam String storeID,
                                         @RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam(required = false, defaultValue = "ID") SearchType.ORDER sortBy,
                                         @RequestParam(required = false, defaultValue = "true") Boolean sortAsc,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Manager")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.equals("RECEIVEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "receivedDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("PROGRESSSTATUS")) {
            paging = util.makePaging(pageNo, pageSize, "progressStatus", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        return orderService.getWaitingOrder(storeID, paging);
    }

    @GetMapping(value = "/orderDetail/{orderID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderDetailModel> getOrderDetailByOrderID(@PathVariable("orderID") String orderID) {
        List<ShowOrderDetailModel> model = orderService.getOrderDetailByOrderID(orderID);
        return model;
    }

    @GetMapping(value = "/getOrderDetailByIsFeedback", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderDetailModel> getOrderDetailByIsFeedback(@RequestParam(required = false) String isFeedback,
                                                          @RequestParam int pageNo,
                                                          @RequestParam int pageSize,
                                                          @RequestParam(required = false, defaultValue = "ID") SearchType.ORDER sortBy,
                                                          @RequestParam(required = false, defaultValue = "true") Boolean sortAsc,
                                                          HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer") && !roleName.equalsIgnoreCase("Staff")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }

        Pageable paging;
        if(sortBy.equals("CREATEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "createdDate", sortAsc);
        } else if(sortBy.equals("RECEIVEDDATE")) {
            paging = util.makePaging(pageNo, pageSize, "receivedDate", sortAsc);
        } else if(sortBy.toString().equalsIgnoreCase("PROGRESSSTATUS")) {
            paging = util.makePaging(pageNo, pageSize, "progressStatus", sortAsc);
        } else {
            paging = util.makePaging(pageNo, pageSize, sortBy.toString().toLowerCase(), sortAsc);
        }

        List<ShowOrderDetailModel> model = orderService.getOrderDetailByIsFeedback(isFeedback, paging);
        return model;
    }

    @GetMapping(value = "/getStaffForOrder", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<GetStaffModel> getStaffForOrder() {
        return orderService.getStaffForOrder();
    }

    @PutMapping(value = "/updateIsPaid", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String updateIsPaid(@RequestParam String orderID,
                        @RequestParam Boolean isPaid) {
        return orderService.updateIsPaid(orderID, isPaid);
    }
}
