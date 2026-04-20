package com.example.hexashop_project.controller.customer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hexashop_project.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/cancel/{orderCode}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderCode) {
        String result = orderService.cancelOrder(orderCode);
        
        Map<String, Object> response = new HashMap<>();
        if ("SUCCESS".equals(result)) {
            response.put("status", "success");
            response.put("message", "Order has been cancelled successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
