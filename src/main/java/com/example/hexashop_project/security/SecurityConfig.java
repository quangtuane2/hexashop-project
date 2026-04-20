package com.example.hexashop_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Công cụ mã hóa mật khẩu (Không bao giờ được lưu mật khẩu thô vào DB)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình các bộ lọc phân quyền
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tạm thời disable CSRF để dễ test API/Giỏ hàng
            
            // PHÂN QUYỀN ĐƯỜNG DẪN (ROUTING)
            .authorizeHttpRequests(auth -> auth
                // Các đường dẫn cho phép TẤT CẢ mọi người vào (kể cả khách vãng lai)
                .requestMatchers("/", "/index", "/assets/**", "/UploadFiles/**" , "/login", "/register", "/about", "/contact").permitAll()
                .requestMatchers("/men/**", "/women/**", "/kids/**", "/single-product/**").permitAll()
                .requestMatchers("/cart", "/api/cart/**").permitAll() // Mở giỏ hàng cho khách vãng lai
                
                // Các đường dẫn bắt buộc phải có quyền ADMIN mới được vào
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                
                // Tất cả các đường dẫn khác (vd: /checkout, /profile) bắt buộc phải Đăng nhập
                .anyRequest().authenticated()
            )
            
            // CẤU HÌNH FORM ĐĂNG NHẬP
            .formLogin(form -> form
                .loginPage("/login") // Đường dẫn tới trang login 
                .loginProcessingUrl("/do-login") // Đường dẫn HTML Form gửi data lên (Security tự xử lý)
                .defaultSuccessUrl("/", true) // Đăng nhập thành công thì về Trang chủ
                .failureUrl("/login?error=true") // Đăng nhập sai mật khẩu thì báo lỗi
                .permitAll()
            )
            
            // CẤU HÌNH ĐĂNG XUẤT
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // Đăng xuất xong về Trang chủ
                .permitAll()
            );

        return http.build();
    }
}
