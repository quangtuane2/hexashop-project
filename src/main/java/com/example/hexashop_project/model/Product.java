package com.example.hexashop_project.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_product")
public class Product extends BaseModel {

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "avatar", length = 255, nullable = true)
    private String avatar;

    @Column(name = "price", precision = 38, scale = 2, nullable = true)
    private BigDecimal price;

    @Column(name = "sale_price", precision = 38, scale = 2, nullable = true)
    private BigDecimal salePrice;

    @Column(name = "is_hot", nullable = true)
    private Boolean isHot;

    @Column(name = "short_description", length = 500, nullable = true)
    private String shortDescription;

    @Column(name = "detail_description", length = 1000, nullable = true)
    private String detailDescription;

    @Column(name = "seo", length = 500, nullable = true)
    private String seo;

    // Khóa ngoại: Nhiều Sản phẩm thuộc 1 Danh mục 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    
    // Ở Entity Product.java
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> productImages = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> reviews = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public Boolean getIsHot() { return isHot; }
    public void setIsHot(Boolean isHot) { this.isHot = isHot; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDetailDescription() { return detailDescription; }
    public void setDetailDescription(String detailDescription) { this.detailDescription = detailDescription; }

    public String getSeo() { return seo; }
    public void setSeo(String seo) { this.seo = seo; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Set<ProductImage> getProductImages() { return productImages; }
    public void setProductImages(Set<ProductImage> productImages) { this.productImages = productImages; }
	public List<ProductVariant> getVariants() {
		return variants;
	}
	public void setVariants(List<ProductVariant> variants) {
		this.variants = variants;
	}
	public List<ProductReview> getReviews() {
		return reviews;
	}
	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}
}
