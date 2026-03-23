package com.example.hexashop_project.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // Ánh xạ URL trang chủ (http://localhost:9090/ hoặc http://localhost:9090/index)
    @GetMapping({"/", "/index"})
    public String index() {
        // Trỏ tới file: src/main/resources/templates/customer/index.html
        return "customer/index"; 
    }

    @GetMapping("/men")
    public String men() {
        return "customer/men";
    }

    @GetMapping("/women")
    public String women() {
        return "customer/women";
    }

    @GetMapping("/kids")
    public String kids() {
        return "customer/kids";
    }
    
    @GetMapping("/cart")
    public String cart() {
        return "customer/cart"; 
    }

    @GetMapping("/about")
    public String about() {
        return "customer/aboutUs"; 
    }

    @GetMapping("/contact")
    public String contact() {
        return "customer/contact";
    }

    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @GetMapping("/register")
    public String register() {
        return "customer/register";
    }
    
    @GetMapping("/order-tracking")
    public String orderTracking() {
        return "customer/order-tracking"; 
    }
    
    @GetMapping("/single-product")
    public String singleProduct(@RequestParam(name = "id", required = false) Long id) {
        return "customer/single-product"; 
    }
}
