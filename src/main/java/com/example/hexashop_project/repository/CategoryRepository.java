package com.example.hexashop_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.Category;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Spring Data JPA đã bao hàm sẵn các hàm như: save(), findAll(), findById(), deleteById()...
	// Kiểm tra tên danh mục đã tồn tại chưa
    boolean existsByName(String name);

    // Tìm kiếm kết hợp (Tên, Từ ngày, Đến ngày) + Phân trang & Sắp xếp
    // Lọc bỏ những Category có status = false (đã bị xóa mềm)
    @Query("SELECT c FROM Category c WHERE c.status = true AND " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:fromDate IS NULL OR c.createDate >= :fromDate) AND " +
           "(:toDate IS NULL OR c.createDate <= :toDate)")
    Page<Category> searchCategories(
            @Param("name") String name, 
            @Param("fromDate") Date fromDate, 
            @Param("toDate") Date toDate, 
            Pageable pageable);
}
