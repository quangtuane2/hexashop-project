package com.example.hexashop_project.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_sale_order_product")
public class SaleOrderProduct extends BaseModel {

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", precision = 38, scale = 2)
    private BigDecimal price;

    @Column(name = "name", length = 300)
    private String name; // Tên sản phẩm lưu tại thời điểm mua

    @Column(name = "description", length = 500)
    private String description;

    // Khóa ngoại: Chi tiết này thuộc Hóa đơn nào 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    // Khóa ngoại: Chi tiết này là Sản phẩm nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
