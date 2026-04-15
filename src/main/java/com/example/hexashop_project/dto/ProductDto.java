package com.example.hexashop_project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.hexashop_project.model.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
	
	private Integer id;

	@NotEmpty(message = "The product name is required")
	private String name;

	// Dùng để hứng file ảnh upload từ form HTML
	private MultipartFile avatarFile;
	
	// Dùng để lưu đường dẫn ảnh hiển thị
	private String avatar;
	
	
	// Nhận danh sách các chữ (Màu, Size, Title...)
    private List<ProductVariantDto> variants = new ArrayList<>();
    
    // Nhận danh sách thông tin text của ảnh phụ (Title)
    private List<ProductImageDto> productImages = new ArrayList<>();

    // Nhận danh sách các FILE ảnh phụ được upload lên
    private List<MultipartFile> imageFiles = new ArrayList<>();

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
	private BigDecimal price;

	private BigDecimal salePrice;

	private Boolean isHot = Boolean.FALSE;

	private String shortDescription;
	
	private String detailDescription;
	
	private String seo;

	@NotNull(message = "Please select a category")
	private Integer categoryId; // Hứng ID của danh mục từ thẻ <select>

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate updateDate;

	private Boolean status = Boolean.TRUE;

	public ProductDto() {
		super();
	}
	
	public ProductDto(Product product) {
	    this.name = product.getName();
	    this.price = product.getPrice();
	    this.salePrice = product.getSalePrice();
	    this.isHot = product.getIsHot();
	    this.shortDescription = product.getShortDescription();
	    this.detailDescription = product.getDetailDescription();
	    this.status = product.getStatus();
        
	    if (product.getCategory() != null) {
	        this.categoryId = product.getCategory().getId();
	    }
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public MultipartFile getAvatarFile() { return avatarFile; }
	public void setAvatarFile(MultipartFile avatarFile) { this.avatarFile = avatarFile; }

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

	public Integer getCategoryId() { return categoryId; }
	public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

	public LocalDate getCreateDate() { return createDate; }
	public void setCreateDate(LocalDate createDate) { this.createDate = createDate; }

	public LocalDate getUpdateDate() { return updateDate; }
	public void setUpdateDate(LocalDate updateDate) { this.updateDate = updateDate; }

	public Boolean getStatus() { return status; }
	public void setStatus(Boolean status) { this.status = status; }

	public List<ProductVariantDto> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariantDto> variants) {
		this.variants = variants;
	}

	public List<ProductImageDto> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImageDto> productImages) {
		this.productImages = productImages;
	}

	public List<MultipartFile> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<MultipartFile> imageFiles) {
		this.imageFiles = imageFiles;
	}
	
}
