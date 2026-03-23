package com.example.hexashop_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Thêm hàm tùy chọn để sau này làm chức năng Đăng nhập
    User findByUsername(String username);
}
