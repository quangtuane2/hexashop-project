package com.example.hexashop_project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
