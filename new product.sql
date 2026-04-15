
-- 1. Bảng lưu Biến thể (Màu sắc, Kích thước, Số lượng)
CREATE TABLE tbl_product_variants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    color VARCHAR(50) NOT NULL,
    size VARCHAR(50) NOT NULL,
    stock_quantity INT DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 2. Bảng lưu Đánh giá sao và Bình luận
CREATE TABLE tbl_product_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT,                  -- Liên kết với bảng User (nếu khách hàng phải đăng nhập mới được bình luận)
    reviewer_name VARCHAR(100),   -- Tên người bình luận (dùng nếu cho khách vãng lai bình luận)
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5), -- Số sao (bắt buộc từ 1 đến 5)
    comment TEXT,                 -- Nội dung bình luận
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Tự động lưu thời gian bình luận
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);