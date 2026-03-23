-- Tạo Database nếu chưa có và chọn để sử dụng
CREATE DATABASE IF NOT EXISTS `hexashop-project` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE `hexashop-project`;

-- =================================================================
-- PHẦN 1: TẠO CÁC BẢNG ĐỘC LẬP (KHÔNG CHỨA KHÓA NGOẠI)
-- =================================================================

-- 1. Bảng Vai trò (Role)
CREATE TABLE `tbl_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 2. Bảng Người dùng (User)
CREATE TABLE `tbl_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(120) COLLATE utf8mb4_bin NOT NULL,
  `firstname` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `avatar` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `mobile` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL,
  `address` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 3. Bảng Trạng thái đơn hàng (Order Status)
CREATE TABLE `tbl_order_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(300) COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 4. Bảng Liên hệ (Contact Us)
CREATE TABLE `tbl_contact` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `message` text COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- =================================================================
-- PHẦN 2: TẠO CÁC BẢNG PHỤ THUỘC (CÓ KHÓA NGOẠI)
-- =================================================================

-- 5. Bảng Phân quyền (User - Role)
CREATE TABLE `tbl_user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`),
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 6. Bảng Danh mục (Category) - Có khóa ngoại tự chiếu chính nó (parent_id)
CREATE TABLE `tbl_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int DEFAULT NULL, -- Danh mục cha (nếu có)
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_cat_parent` FOREIGN KEY (`category_id`) REFERENCES `tbl_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 7. Bảng Sản phẩm chính (Product)
CREATE TABLE `tbl_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL, -- Khóa ngoại trỏ về Category
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `sale_price` decimal(38,2) DEFAULT NULL,
  `is_hot` bit(1) DEFAULT NULL,
  `short_description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `detail_description` varchar(1000) COLLATE utf8mb4_bin DEFAULT NULL,
  `seo` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_prod_cat` FOREIGN KEY (`category_id`) REFERENCES `tbl_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 8. Bảng Thư viện ảnh Sản phẩm (Product Image)
CREATE TABLE `tbl_product_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL, -- Khóa ngoại trỏ về Product
  `title` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `path` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_img_prod` FOREIGN KEY (`product_id`) REFERENCES `tbl_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 9. Bảng Hóa đơn tổng (Sale Order)
CREATE TABLE `tbl_sale_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(60) COLLATE utf8mb4_bin NOT NULL,
  `total` decimal(38,2) DEFAULT NULL,
  `customer_name` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `customer_mobile` varchar(120) COLLATE utf8mb4_bin DEFAULT NULL,
  `customer_email` varchar(120) COLLATE utf8mb4_bin DEFAULT NULL,
  `customer_address` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `order_status` int DEFAULT NULL, -- Khóa ngoại trỏ về Order Status
  `delivery_date` datetime DEFAULT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_status` FOREIGN KEY (`order_status`) REFERENCES `tbl_order_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- 10. Bảng Chi tiết Hóa đơn (Sale Order Product)
CREATE TABLE `tbl_sale_order_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sale_order_id` int NOT NULL, -- Khóa ngoại trỏ về Sale Order
  `product_id` int NOT NULL,    -- Khóa ngoại trỏ về Product
  `quantity` int NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `name` varchar(300) COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sop_order` FOREIGN KEY (`sale_order_id`) REFERENCES `tbl_sale_order` (`id`),
  CONSTRAINT `fk_sop_prod` FOREIGN KEY (`product_id`) REFERENCES `tbl_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;