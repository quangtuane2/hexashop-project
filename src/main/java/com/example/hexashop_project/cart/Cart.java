package com.example.hexashop_project.cart;

import com.example.hexashop_project.model.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_cart")
public class Cart {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ 1-1 với User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
	
    // Quan hệ 1-Nhiều với CartItem (1 Giỏ có nhiều Món)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    // Tính tổng tiền toàn bộ giỏ hàng
    @Transient
    public Double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubTotal();
        }
        return total;
    }

    // Tính tổng số lượng (Để hiển thị số nhỏ nhỏ trên icon Giỏ hàng ở Header)
    @Transient
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }
}
