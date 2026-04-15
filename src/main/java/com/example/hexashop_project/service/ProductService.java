package com.example.hexashop_project.service;

import java.util.List;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.hexashop_project.dto.ProductDto;
import com.example.hexashop_project.dto.ProductVariantDto;
import com.example.hexashop_project.model.Category;
import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.model.ProductImage;
import com.example.hexashop_project.model.ProductVariant;
import com.example.hexashop_project.repository.CategoryRepository;
import com.example.hexashop_project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private FileUploadService fileUploadService;

    // Lấy tất cả sản phẩm
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Tìm sản phẩm theo ID
//    public Product getById(Integer id) {
//        Optional<Product> product = productRepository.findById(id);
//        return product.orElse(null);
//    }
    
    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepository.findActiveProductsByCategory(categoryId, pageable);
    }

    // Lấy danh sách Nổi Bật (Giới hạn số lượng)
    public List<Product> getFeaturedProducts(Integer categoryId, int limit) {
        return productRepository.findFeaturedProducts(categoryId, PageRequest.of(0, limit));
    }

    // Lấy danh sách Flash Sale (Giới hạn số lượng)
    public List<Product> getFlashSaleProducts(Integer categoryId, int limit) {
        return productRepository.findFlashSaleProducts(categoryId, PageRequest.of(0, limit));
    }
    // Lưu hoặc cập nhật sản phẩm
    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm theo ID
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
    
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    // Xử lý tìm kiếm và phân trang
    public Page<Product> searchProducts(Integer categoryId, String name, Pageable pageable) {
        return productRepository.searchProducts(categoryId, name, pageable);
    }

    // XÓA MỀM
    public void inactive(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStatus(false);
            productRepository.save(product);
        }
    }
    
    @Transactional
    public void saveProduct(ProductDto dto) throws Exception {
        Product product;
        boolean isUpdate = (dto.getId() != null); // Biến kiểm tra xem là Thêm mới hay Sửa
        
        if (isUpdate) {
            // NẾU LÀ CẬP NHẬT
            product = productRepository.findById(dto.getId()).orElseThrow(() -> new Exception("Product not found"));
            product.getVariants().clear(); // Xóa các phân loại cũ để ghi đè cái mới
            product.setUpdateDate(new Date());
        } else {
            // NẾU LÀ THÊM MỚI
            product = new Product();
            product.setCreateDate(new Date());
        }

        // AP THÔNG TIN CƠ BẢN
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setShortDescription(dto.getShortDescription());
        product.setDetailDescription(dto.getDetailDescription());
        product.setIsHot(dto.getIsHot() != null ? dto.getIsHot() : false);
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : true);

        // MAP CATEGORY
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        
        // XỬ LÝ UPLOAD ẢNH ĐẠI DIỆN (AVATAR)
        if (dto.getAvatarFile() != null && !dto.getAvatarFile().isEmpty()) {
            String avatarPath = fileUploadService.uploadFile(dto.getAvatarFile());
            product.setAvatar(avatarPath); 
        }
        
        // MAP BIẾN THỂ (VARIANTS)
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            for (ProductVariantDto vDto : dto.getVariants()) {
                if (vDto.getColor() != null && !vDto.getColor().trim().isEmpty()) { 
                    ProductVariant variant = new ProductVariant();
                    variant.setColor(vDto.getColor());
                    variant.setSize(vDto.getSize());
                    variant.setStockQuantity(vDto.getStockQuantity());
                    
                    variant.setProduct(product); 
                    product.getVariants().add(variant); 
                }
            }
        }

        // XỬ LÝ UPLOAD ẢNH PHỤ (IMAGES)
        if (dto.getImageFiles() != null && !dto.getImageFiles().isEmpty()) {
            for (int i = 0; i < dto.getImageFiles().size(); i++) {
                MultipartFile file = dto.getImageFiles().get(i);
                
                if (!file.isEmpty()) {
                    // Sửa lại gọi đúng hàm của fileUploadService
                    String filePath = fileUploadService.uploadFile(file); 
                    
                    ProductImage image = new ProductImage();
                    image.setPath(filePath);
                    
                    if (dto.getProductImages() != null && dto.getProductImages().size() > i) {
                        image.setTitle(dto.getProductImages().get(i).getTitle());
                    }
                    
                    image.setProduct(product);
                    product.getProductImages().add(image); 
                }
            }
        }

        // LƯU VÀO DATABASE
        productRepository.save(product);
    }
  
}
