package com.example.hexashop_project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hexashop_project.model.User;
import com.example.hexashop_project.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
