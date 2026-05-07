package com.example.hexashop_project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.SaleOrder;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Integer> {
    SaleOrder findByCode(String code);

    // Tìm đơn hàng của khách theo email
    List<SaleOrder> findByCustomerEmailOrderByCreateDateDesc(String email);

    // Xác thực: tìm đơn hàng theo email + mã đơn (dùng cho khách vãng lai tra cứu)
    SaleOrder findByCustomerEmailAndCode(String customerEmail, String code);
}

