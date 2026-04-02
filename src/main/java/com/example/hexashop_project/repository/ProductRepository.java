package com.example.hexashop_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Kiểm tra tên sản phẩm đã tồn tại chưa
    boolean existsByName(String name);

    // Tìm kiếm kết hợp (Lọc theo Danh mục, Tìm theo Tên) + Phân trang
    // Bỏ qua những Sản phẩm đã bị xóa mềm (status = false)
    @Query("SELECT p FROM Product p WHERE p.status = true AND " +
           "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) AND " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Product> searchProducts(
            @Param("categoryId") Integer categoryId, 
            @Param("name") String name, 
            Pageable pageable);
    
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.category c " +
           "LEFT JOIN c.parentCategory pc " +
           "LEFT JOIN pc.parentCategory ppc " +
           "WHERE p.status = true AND " +
           "(c.id = :categoryId OR pc.id = :categoryId OR ppc.id = :categoryId)")
    Page<Product> findActiveProductsByCategory(@Param("categoryId") Integer categoryId, Pageable pageable);
    
 // Lọc sản phẩm NỔI BẬT (isHot = true)
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.category c LEFT JOIN c.parentCategory pc LEFT JOIN pc.parentCategory ppc " +
           "WHERE p.status = true AND p.isHot = true AND " +
           "(c.id = :categoryId OR pc.id = :categoryId OR ppc.id = :categoryId) " +
           "ORDER BY p.id DESC")
    List<Product> findFeaturedProducts(@Param("categoryId") Integer categoryId, Pageable pageable);

    //Lọc sản phẩm FLASH SALE (Có salePrice > 0)
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN p.category c LEFT JOIN c.parentCategory pc LEFT JOIN pc.parentCategory ppc " +
           "WHERE p.status = true AND p.salePrice > 0 AND p.salePrice < p.price AND " +
           "(c.id = :categoryId OR pc.id = :categoryId OR ppc.id = :categoryId) " +
           "ORDER BY p.salePrice ASC")
    List<Product> findFlashSaleProducts(@Param("categoryId") Integer categoryId, Pageable pageable);
}
