package com.example.hexashop_project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hexashop_project.dto.UserRegisterDTO;
import com.example.hexashop_project.model.Role;
import com.example.hexashop_project.model.User;
import com.example.hexashop_project.repository.RoleRepository;
import com.example.hexashop_project.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Công cụ mã hóa mật khẩu
    
    @Transactional
    public String registerUser(UserRegisterDTO dto) {
        // Kiểm tra Email/Username đã tồn tại chưa
        if (userRepository.existsByUsername(dto.getEmail())) {
            return "Email này đã được sử dụng!";
        }

        // Kiểm tra mật khẩu nhập lại
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return "Mật khẩu nhập lại không khớp!";
        }

        // Tạo User mới
        User user = new User();
        user.setUsername(dto.getEmail()); // Lấy email làm username luôn cho tiện
        user.setEmail(dto.getEmail());
        
        // Tách Họ và Tên từ fullName (Vd: "Nguyen Van A")
        String[] names = dto.getFullName().split(" ", 2);
        if (names.length > 1) {
            user.setFirstname(names[0]); // Nguyen
            user.setLastname(names[1]);  // Van A
        } else {
            user.setFirstname(dto.getFullName());
        }

        // MÃ HÓA MẬT KHẨU TRƯỚC KHI LƯU DB
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(true); // Kích hoạt tài khoản

        // Cấp quyền mặc định là Khách hàng (ROLE_USER)
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            // Nếu DB chưa có quyền ROLE_USER thì tự động tạo luôn (Tránh lỗi)
            userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }
        user.getRoles().add(userRole);

        // Lưu vào Database
        userRepository.save(user);
        return "SUCCESS";
    }

    // Lấy tất cả người dùng
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Lấy người dùng theo ID
    public User getById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    // Lưu hoặc cập nhật người dùng (Đăng ký)
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    // Xóa người dùng theo ID
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
    
    // Tìm User bằng username (Dùng cho Đăng nhập)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    
}
