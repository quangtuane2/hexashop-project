package com.example.hexashop_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_product_image")
public class ProductImage extends BaseModel {

    @Column(name = "title", length = 255, nullable = true)
    private String title;

    @Column(name = "path", length = 255, nullable = false)
    private String path;

    // Khóa ngoại: Nhiều Ảnh thuộc về 1 Sản phẩm 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
