package com.example.hexashop_project.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Đặt prefix /admin cho toàn bộ class 
public class AdminController {

    // Ánh xạ URL (http://localhost:9090/admin hoặc http://localhost:9090/admin/dashboard)
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard() {
        // Trỏ tới file: src/main/resources/templates/administrator/dashboard.html
        return "administrator/dashboard"; 
    }
}
