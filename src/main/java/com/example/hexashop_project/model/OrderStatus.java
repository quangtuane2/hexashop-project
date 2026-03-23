package com.example.hexashop_project.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_order_status")
public class OrderStatus extends BaseModel {

    @Column(name = "name", length = 300, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderStatus")
    private Set<SaleOrder> saleOrders = new HashSet<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<SaleOrder> getSaleOrders() { return saleOrders; }
    public void setSaleOrders(Set<SaleOrder> saleOrders) { this.saleOrders = saleOrders; }
}
