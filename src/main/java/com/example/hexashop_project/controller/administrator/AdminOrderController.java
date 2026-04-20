package com.example.hexashop_project.controller.administrator;

import com.example.hexashop_project.model.SaleOrder;
import com.example.hexashop_project.repository.SaleOrderRepository;
import com.example.hexashop_project.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private SaleOrderRepository saleOrderRepository;
    
    @Autowired
    private OrderService orderService;

    @GetMapping({"/", ""})
    public String listOrders(Model model) {
        // Lấy toàn bộ đơn hàng, sắp xếp mới nhất lên đầu
        List<SaleOrder> orders = saleOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        
        model.addAttribute("orders", orders);
        
        // Trả về file HTML nằm ở thư mục templates/admin/order-list.html
        return "admin/orders/order-list"; 
    }
    
    // Trang xem chi tiết đơn hàng
    @GetMapping("/detail")
    public String viewOrderDetail(@RequestParam Integer id, Model model) {
        SaleOrder order = saleOrderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/admin/orders/order-list";

        model.addAttribute("order", order);
        return "admin/orders/order-detail";
    }

    // Xử lý cập nhật trạng thái đơn hàng
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer orderId, @RequestParam String statusName, RedirectAttributes ra) {
        try {
            orderService.updateOrderStatus(orderId, statusName);
            ra.addFlashAttribute("successMsg", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/detail?id=" + orderId;
    }
}
