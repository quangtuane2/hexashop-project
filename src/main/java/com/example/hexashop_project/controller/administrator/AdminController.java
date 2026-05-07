package com.example.hexashop_project.controller.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.example.hexashop_project.repository.SaleOrderRepository;
import com.example.hexashop_project.repository.UserRepository;
import com.example.hexashop_project.model.SaleOrder;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin") // Đặt prefix /admin cho toàn bộ class 
public class AdminController {
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private UserRepository userRepository;

    // Ánh xạ URL (http://localhost:9090/admin hoặc http://localhost:9090/admin/dashboard)
    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        BigDecimal earnings = saleOrderRepository.getMonthlyEarnings();
        Long annualOrders = saleOrderRepository.getAnnualOrders();
        Long totalUsers = userRepository.count();
        Long pendingRequests = saleOrderRepository.getPendingOrders();
        List<SaleOrder> recentOrders = saleOrderRepository.findTop5ByOrderByCreateDateDesc();

        model.addAttribute("earnings", earnings != null ? earnings : BigDecimal.ZERO);
        model.addAttribute("annualOrders", annualOrders != null ? annualOrders : 0L);
        model.addAttribute("totalUsers", totalUsers != null ? totalUsers : 0L);
        model.addAttribute("pendingRequests", pendingRequests != null ? pendingRequests : 0L);
        model.addAttribute("recentOrders", recentOrders);

        // Trỏ tới file: src/main/resources/templates/administrator/dashboard.html
        return "administrator/dashboard"; 
    }
}
