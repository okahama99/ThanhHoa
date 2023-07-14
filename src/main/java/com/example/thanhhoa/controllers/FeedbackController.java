package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.FeedbackModels.CreateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.CreateOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowRatingModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.UpdateOrderFeedbackModel;
import com.example.thanhhoa.services.feedback.FeedbackService;
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
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private Util util;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping(value = "/createOrderFB", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createOrderFeedback(@RequestBody CreateOrderFeedbackModel createOrderFeedbackModel,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.createOrder(createOrderFeedbackModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/updateOrderFB",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateOrderFeedback(@RequestBody UpdateOrderFeedbackModel updateOrderFeedbackModel,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.updateOrder(updateOrderFeedbackModel);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/deleteOrderFB/{orderFeedbackID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteOrderFeedback(@PathVariable(name = "orderFeedbackID") String orderFeedbackID,
                                         HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.deleteOrder(orderFeedbackID);
        if(result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping(value = "/orderFeedback", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderFeedbackModel> getAllOrderFeedback(@RequestParam int pageNo,
                                                     @RequestParam int pageSize,
                                                     @RequestParam(required = false, defaultValue = "ID") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        return feedbackService.getAllOrderFeedback(util.makePaging(pageNo, pageSize, sortBy.toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/orderFeedback/{orderFeedbackID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getOrderFeedbackByID(@PathVariable("orderFeedbackID") String orderFeedbackID) {
        ShowOrderFeedbackModel model = feedbackService.getOrderFeedbackByID(orderFeedbackID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Feedback không tồn tại.");
    }

    @GetMapping(value = "/orderFeedback/{username}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getOrderFeedbackByUsername(@PathVariable("username") String username) {
        ShowOrderFeedbackModel model = feedbackService.getOrderFeedbackByUsername(username);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Feedback không tồn tại.");
    }

    @GetMapping(value = "/orderFeedback/{plantID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowOrderFeedbackModel> getOrderFeedbackByPlantID(@PathVariable("plantID") String plantID,
                                                           @RequestParam int pageNo,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false, defaultValue = "ID") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        return feedbackService.getOrderFeedbackByPlantID(plantID, util.makePaging(pageNo, pageSize, sortBy.toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/orderFeedback/{orderDetailID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getOrderFeedbackByOrderDetailID(@PathVariable("orderID") String orderDetailID) {
        ShowOrderFeedbackModel model = feedbackService.getOrderFeedbackByOrderDetailID(orderDetailID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Feedback không tồn tại.");
    }

    // ------------------------------------- CONTRACT ------------------------------------------------------------

    @PostMapping(value = "/createContractFB",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createContractFeedback(@RequestBody CreateContractFeedbackModel createContractFeedbackModel,
                                                      HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.createContract(createContractFeedbackModel, jwtUtil.getUserIDFromRequest(request));
        if(result.equals("Tạo thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping(value = "/updateContractFB",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateContractFeedback(@RequestBody UpdateContractFeedbackModel updateContractFeedbackModel,
                                                      HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.updateContract(updateContractFeedbackModel);
        if(result.equals("Chỉnh sửa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping(value = "/deleteContractFB/{contractFeedbackID}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteContractFeedback(@PathVariable(name = "contractFeedbackID") String contractFeedbackID,
                                                      HttpServletRequest request) {
        String roleName = jwtUtil.getRoleNameFromRequest(request);
        if(!roleName.equalsIgnoreCase("Customer")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "-----------------------------------Người dùng không có quyền truy cập---------------------------");
        }
        String result = feedbackService.deleteContract(contractFeedbackID);
        if(result.equals("Xóa thành công.")) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping(value = "/contractFeedback", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ShowContractFeedbackModel> getAllContractFeedback(@RequestParam int pageNo,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false, defaultValue = "ID") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "true") Boolean sortAsc) {
        return feedbackService.getAllContractFeedback(util.makePaging(pageNo, pageSize, sortBy.toLowerCase(), sortAsc));
    }

    @GetMapping(value = "/contractFeedback/{contractFeedbackID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getContractFeedbackByID(@PathVariable("contractFeedbackID") String contractFeedbackID) {
        ShowContractFeedbackModel model = feedbackService.getContractFeedbackByID(contractFeedbackID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Contract không tồn tại.");
    }

    @GetMapping(value = "/contractFeedback/{contractID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getContractFeedbackByContractID(@PathVariable("contractID") String contractID) {
        ShowContractFeedbackModel model = feedbackService.getContractFeedbackByContractID(contractID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Contract không tồn tại.");
    }

    @GetMapping(value = "/getRating", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getRating() {
        return ResponseEntity.ok().body(feedbackService.getRating());
    }
}
