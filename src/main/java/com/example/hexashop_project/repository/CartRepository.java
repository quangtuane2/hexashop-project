package com.example.hexashop_project.repository;

import com.example.hexashop_project.cart.Cart;
import com.example.hexashop_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Tìm giỏ hàng theo User
    Cart findByUser(User user);
}
