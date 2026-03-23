package com.example.hexashop_project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hexashop_project.model.SaleOrder;
import com.example.hexashop_project.repository.SaleOrderRepository;

@Service
public class SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    public List<SaleOrder> findAll() {
        return saleOrderRepository.findAll();
    }

    public SaleOrder getById(Integer id) {
        return saleOrderRepository.findById(id).orElse(null);
    }

    public SaleOrder getByCode(String code) {
        return saleOrderRepository.findByCode(code);
    }

    public SaleOrder saveOrUpdate(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }
}
