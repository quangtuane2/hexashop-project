package com.example.hexashop_project.dto;

import jakarta.validation.constraints.NotEmpty;

public class ProductVariantDto {
	
	private Integer id; // Dùng khi Update
	
	@NotEmpty(message = "The product color is required")
    private String color;
	
	@NotEmpty(message = "The product size is required")
    private String size;
	
	@NotEmpty(message = "The product quantity is required")
    private Integer stockQuantity;

	public ProductVariantDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	
}
