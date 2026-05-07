package com.example.hexashop_project.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Lưu token xác minh email gửi cho user khi đăng ký.
 * Mỗi token là một UUID ngẫu nhiên, có thời hạn 24 giờ.
 */
@Entity
@Table(name = "tbl_email_verification_token")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Token UUID ngẫu nhiên (VD: "a1b2c3d4-e5f6-...")
    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    // Liên kết với User cần xác minh
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Thời điểm hết hạn (24 giờ sau khi tạo)
    @Column(name = "expiry_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    // Đã sử dụng chưa (tránh dùng lại nhiều lần)
    @Column(name = "used")
    private Boolean used = false;

    public EmailVerificationToken() {}

    public EmailVerificationToken(String token, User user, Date expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.used = false;
    }

    // ── Getters & Setters ──

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public Boolean getUsed() { return used; }
    public void setUsed(Boolean used) { this.used = used; }

    /** Kiểm tra token còn hạn không */
    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}
