package com.example.hexashop_project.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hexashop_project.model.Category;
import com.example.hexashop_project.repository.CategoryRepository;
import com.example.hexashop_project.dto.CategoryDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách tất cả danh mục
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Tìm danh mục theo ID
    public Category getById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null); // Nếu tìm thấy trả về category, không thì trả về null
    }

    // Thêm mới hoặc cập nhật danh mục
    public Category saveOrUpdate(Category category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục theo ID
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }

    // Xử lý tìm kiếm và phân trang
    public Page<Category> findAllSortPageableAndSearch(String name, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        // Chuyển đổi LocalDateTime sang java.util.Date để query trong DB
        Date from = fromDate != null ? Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
        Date to = toDate != null ? Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()) : null;
        
        return categoryRepository.searchCategories(name, from, to, pageable);
    }

    // Thêm mới danh mục từ DTO
    public Category insert(CategoryDto dto) throws Exception {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());
        category.setCreateDate(new Date()); // Lưu ngày hiện tại
        
        // Xử lý danh mục cha
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            Category parent = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            category.setParentCategory(parent);
        }
        
        return categoryRepository.save(category);
    }

    // Cập nhật danh mục
    public Category update(Integer id, CategoryDto dto) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
        
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());
        category.setUpdateDate(new Date());
        
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0) {
            Category parent = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }
        
        return categoryRepository.save(category);
    }

    // Xóa mềm (Inactive)
    public void inactive(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setStatus(false); // Đổi trạng thái thành false thay vì xóa hẳn khỏi DB
            categoryRepository.save(category);
        }
    }
}
