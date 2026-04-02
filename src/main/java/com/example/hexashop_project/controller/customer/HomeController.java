package com.example.hexashop_project.controller.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	
	@Autowired
    private ProductService productService;

    // Ánh xạ URL trang chủ (http://localhost:9090/ hoặc http://localhost:9090/index)
    @GetMapping({"/", "/index"})
    public String index() {
        // Trỏ tới file: src/main/resources/templates/customer/index.html
        return "customer/index"; 
    }
    
    @GetMapping("/men")
    public String men(Model model, @RequestParam(defaultValue = "0") int page) {
        // Lấy tất cả sản phẩm Lastest
    	Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productService.getProductsByCategory(1, pageable);
        
        // Flash Sale (Lấy 8 sản phẩm có giảm giá)
        List<Product> flashSales = productService.getFlashSaleProducts(1, 8);
        
        // Featured (Lấy 8 sản phẩm đánh dấu Hot)
        List<Product> featuredProducts = productService.getFeaturedProducts(1, 8);
        
        Pageable casualPage = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id"));
        List<Product> casualProducts = productService.getProductsByCategory(13, casualPage).getContent();
        
        // Truyền dữ liệu ra View
        model.addAttribute("productPage", productPage);
        model.addAttribute("menProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        
        model.addAttribute("flashSales", flashSales);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("casualProducts", casualProducts);
        
        return "customer/men";
    }

    @GetMapping("/women")
    public String women(Model model, @RequestParam(defaultValue = "0") int page) {
        // Women's Latest 
        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productService.getProductsByCategory(2, pageable);
        
        // Flash Sale (Lấy 8 sản phẩm Đồ Nữ có giảm giá)
        List<Product> flashSales = productService.getFlashSaleProducts(2, 8);
        
        // Featured (Lấy 8 sản phẩm Đồ Nữ đánh dấu Hot)
        List<Product> featuredProducts = productService.getFeaturedProducts(2, 8);
        
        // Casual (Lấy ID = 7 là Váy Nữ làm đồ mặc thường ngày)
        Pageable casualPage = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id"));
        List<Product> casualProducts = productService.getProductsByCategory(7, casualPage).getContent();

        // TRUYỀN TẤT CẢ RA GIAO DIỆN
        model.addAttribute("productPage", productPage);
        model.addAttribute("womenProducts", productPage.getContent()); // Latest
        model.addAttribute("currentPage", page);
        
        model.addAttribute("flashSales", flashSales);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("casualProducts", casualProducts);
        
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
