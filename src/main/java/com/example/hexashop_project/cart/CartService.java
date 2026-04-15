package com.example.hexashop_project.cart;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {

    // Lấy giỏ hàng từ Session. Nếu chưa có thì tạo mới một cái giỏ rỗng.
    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("myCart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("myCart", cart);
        }
        return cart;
    }

    // Thêm đồ vào giỏ
    public void addItem(HttpSession session, CartItem newItem) {
        Cart cart = getCart(session);
        boolean isExist = false;

        // Quét xem món này (Cùng ID, cùng Màu, cùng Size) đã có trong giỏ chưa?
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(newItem.getProductId()) &&
                item.getColor().equals(newItem.getColor()) &&
                item.getSize().equals(newItem.getSize())) {
                
                // Nếu có rồi thì chỉ tăng số lượng lên thôi
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                isExist = true;
                break;
            }
        }

        // Nếu là đồ mới , thì quăng thẳng vào giỏ
        if (!isExist) {
            cart.getItems().add(newItem);
        }
    }
    
    // Cập nhật số lượng
    public void updateQuantity(HttpSession session, Integer productId, String color, String size, int newQuantity) {
        Cart cart = getCart(session);
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId) && item.getColor().equals(color) && item.getSize().equals(size)) {
                item.setQuantity(newQuantity);
                break;
            }
        }
    }

    // Xóa món hàng
    public void removeItem(HttpSession session, Integer productId, String color, String size) {
        Cart cart = getCart(session);
        cart.getItems().removeIf(item -> 
            item.getProductId().equals(productId) && 
            item.getColor().equals(color) && 
            item.getSize().equals(size)
        );
    }
}
