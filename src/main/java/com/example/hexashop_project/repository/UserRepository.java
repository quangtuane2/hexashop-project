package com.example.hexashop_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Thêm hàm tùy chọn để sau này làm chức năng Đăng nhập
    User findByUsername(String username);
    
    // Hàm để kiểm tra xem user/email đã bị ai đăng ký chưa
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm kiếm theo username, email, tên hoặc số điện thoại
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(u.email)    LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(u.lastname)  LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "u.mobile LIKE CONCAT('%', :kw, '%')")
    Page<User> searchByKeyword(@Param("kw") String keyword, Pageable pageable);
}
