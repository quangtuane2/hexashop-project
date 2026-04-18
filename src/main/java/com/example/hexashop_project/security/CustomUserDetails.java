package com.example.hexashop_project.security;

import com.example.hexashop_project.model.Role;
import com.example.hexashop_project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Trả về thông tin User thật để sau này lấy ra dùng (vd: lấy avatar, tên...)
    public User getUser() {
        return user;
    }

    // DỊCH QUYỀN (ROLES): Chuyển Set<Role> thành Collection<GrantedAuthority> của Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // DỊCH PASSWORD VÀ USERNAME
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // CÁC TRẠNG THÁI TÀI KHOẢN (Mặc định cứ để true là tài khoản hoạt động bình thường)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return user.getStatus() != null ? user.getStatus() : true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.getStatus() != null ? user.getStatus() : true; }
}
