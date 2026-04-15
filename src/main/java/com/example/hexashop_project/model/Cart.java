package com.example.hexashop_project.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    // Tính tổng tiền toàn bộ giỏ hàng
    public Double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    // Tính tổng số lượng (Để hiển thị số nhỏ nhỏ trên icon Giỏ hàng ở Header)
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }
}
