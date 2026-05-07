package com.example.hexashop_project.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hexashop_project.dto.UserRegisterDTO;
import com.example.hexashop_project.model.EmailVerificationToken;
import com.example.hexashop_project.model.Role;
import com.example.hexashop_project.model.User;
import com.example.hexashop_project.repository.EmailVerificationTokenRepository;
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
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // ĐĂNG KÝ — Tạo user + Gửi email xác minh
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
        user.setUsername(dto.getEmail()); // Email làm username
        user.setEmail(dto.getEmail());

        // Tách Họ và Tên từ fullName
        String[] names = dto.getFullName().split(" ", 2);
        if (names.length > 1) {
            user.setFirstname(names[0]);
            user.setLastname(names[1]);
        } else {
            user.setFirstname(dto.getFullName());
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ★ STATUS = FALSE — chờ xác minh email
        user.setStatus(false);
        user.setCreateDate(new Date());

        // Cấp quyền ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }
        user.getRoles().add(userRole);

        // Lưu user vào DB
        userRepository.save(user);

        // ★ Tạo token xác minh (UUID, hết hạn sau 24h)
        String tokenValue = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        EmailVerificationToken evToken = new EmailVerificationToken(tokenValue, user, cal.getTime());
        tokenRepository.save(evToken);

        // ★ Gửi email xác minh
        emailService.sendVerificationEmail(user, tokenValue);

        return "SUCCESS";
    }

    // XÁC MINH EMAIL — User click link trong email
    @Transactional
    public String verifyEmail(String tokenValue) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenValue);

        if (token == null) {
            return "INVALID"; // Token không tồn tại
        }
        if (token.getUsed()) {
            return "USED"; // Token đã được dùng rồi
        }
        if (token.isExpired()) {
            return "EXPIRED"; // Token hết hạn
        }

        // Kích hoạt tài khoản
        User user = token.getUser();
        user.setStatus(true); // ★ Mở khóa tài khoản
        user.setUpdateDate(new Date());
        userRepository.save(user);

        // Đánh dấu token đã dùng
        token.setUsed(true);
        tokenRepository.save(token);

        return "SUCCESS";
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
