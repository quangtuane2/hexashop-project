package com.example.hexashop_project.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.example.hexashop_project.cart.CartItem;
import com.example.hexashop_project.cart.CartService;
import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.service.ProductService;
import com.example.hexashop_project.cart.AddToCartRequest;
import com.example.hexashop_project.cart.Cart;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService; // Cần gọi DB để check giá trị thật

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, HttpSession session) {
        // Tìm sản phẩm thật trong Database
        Product product = productService.findById(request.getProductId());
        if (product == null) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }

        // Chuyển đổi dữ liệu vào CartItem
        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductAvatar(product.getAvatar());
        
        // Lấy giá khuyến mãi nếu có, không thì lấy giá gốc
        Double finalPrice = (product.getSalePrice() != null && product.getSalePrice() > 0) 
                            ? product.getSalePrice() 
                            : product.getPrice();
        item.setPrice(finalPrice);
        
        item.setColor(request.getColor());
        item.setSize(request.getSize());
        item.setQuantity(request.getQuantity());

        // Quăng vào Service để xử lý lưu Session
        cartService.addItem(session, item);
        
        // Lấy giỏ hàng ra để đếm tổng số món, trả về cho Frontend hiển thị số trên Header
        Cart cart = cartService.getCart(session);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Đã thêm vào giỏ hàng");
        response.put("totalQuantity", cart.getTotalQuantity()); // Trả về tổng số lượng để update icon Giỏ hàng

        return ResponseEntity.ok(response);
    }
}
