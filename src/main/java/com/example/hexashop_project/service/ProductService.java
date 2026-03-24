package com.example.hexashop_project.service;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.hexashop_project.dto.ProductDto;
import com.example.hexashop_project.model.Category;
import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.repository.CategoryRepository;
import com.example.hexashop_project.repository.ProductRepository;

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

    // THÊM MỚI SẢN PHẨM (Kèm Upload Ảnh)
    public Product insert(ProductDto dto) throws Exception {
        Product product = new Product();
        
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setShortDescription(dto.getShortDescription());
        product.setDetailDescription(dto.getDetailDescription());
        product.setIsHot(dto.getIsHot() != null ? dto.getIsHot() : false);
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        product.setCreateDate(new Date());
        
        // Thiết lập Danh mục (Khóa ngoại)
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        
        // XỬ LÝ UPLOAD ẢNH ĐẠI DIỆN
        if (dto.getAvatarFile() != null && !dto.getAvatarFile().isEmpty()) {
            String path = fileUploadService.uploadFile(dto.getAvatarFile());
            product.setAvatar(path); // Lưu đường dẫn "/UploadFiles/..." vào Database
        }
        
        return productRepository.save(product);
    }

    // CẬP NHẬT SẢN PHẨM
    public Product update(Integer id, ProductDto dto) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
        
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setShortDescription(dto.getShortDescription());
        product.setDetailDescription(dto.getDetailDescription());
        product.setIsHot(dto.getIsHot() != null ? dto.getIsHot() : false);
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        product.setUpdateDate(new Date());
        
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        
        // XỬ LÝ UPLOAD ẢNH (Chỉ cập nhật ảnh mới nếu người dùng có chọn file)
        if (dto.getAvatarFile() != null && !dto.getAvatarFile().isEmpty()) {
            String path = fileUploadService.uploadFile(dto.getAvatarFile());
            product.setAvatar(path);
        }
        
        return productRepository.save(product);
    }

    // XÓA MỀM
    public void inactive(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStatus(false);
            productRepository.save(product);
        }
    }
}
