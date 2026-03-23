package com.example.hexashop_project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Tìm sản phẩm theo ID
    public Product getById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    // Lưu hoặc cập nhật sản phẩm
    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm theo ID
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}
