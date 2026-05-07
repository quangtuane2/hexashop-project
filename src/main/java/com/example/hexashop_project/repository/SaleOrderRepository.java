package com.example.hexashop_project.repository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.hexashop_project.model.SaleOrder;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Integer> {
    SaleOrder findByCode(String code);

    // Tìm đơn hàng của khách theo email
    List<SaleOrder> findByCustomerEmailOrderByCreateDateDesc(String email);

    // Xác thực: tìm đơn hàng theo email + mã đơn (dùng cho khách vãng lai tra cứu)
    SaleOrder findByCustomerEmailAndCode(String customerEmail, String code);

    @Query("SELECT SUM(s.total) FROM SaleOrder s WHERE MONTH(s.createDate) = MONTH(CURRENT_DATE) AND YEAR(s.createDate) = YEAR(CURRENT_DATE)")
    BigDecimal getMonthlyEarnings();

    @Query("SELECT COUNT(s) FROM SaleOrder s WHERE YEAR(s.createDate) = YEAR(CURRENT_DATE)")
    Long getAnnualOrders();

    @Query("SELECT COUNT(s) FROM SaleOrder s WHERE s.orderStatus.name = 'Pending' OR s.orderStatus.name = 'Chờ xác nhận'")
    Long getPendingOrders();

    List<SaleOrder> findTop5ByOrderByCreateDateDesc();
}

