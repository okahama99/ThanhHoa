package com.example.thanhhoa.controllers;

import com.example.thanhhoa.dtos.FeedbackModels.ShowContractFeedbackModel;
import com.example.thanhhoa.dtos.FeedbackModels.ShowOrderFeedbackModel;
import com.example.thanhhoa.services.feedback.FeedbackService;
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
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private Util util;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FeedbackService feedbackService;

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

    @GetMapping(value = "/orderFeedback/{orderID}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<Object> getOrderFeedbackByOrderID(@PathVariable("orderID") String orderID) {
        ShowOrderFeedbackModel model = feedbackService.getOrderFeedbackByOrderID(orderID);
        if (model != null) {
            return ResponseEntity.ok().body(model);
        }
        return ResponseEntity.badRequest().body("Feedback không tồn tại.");
    }

    // ------------------------------------- CONTRACT ------------------------------------------------------------

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
}