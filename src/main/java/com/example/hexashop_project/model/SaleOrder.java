package com.example.hexashop_project.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_sale_order")
public class SaleOrder extends BaseModel {

    @Column(name = "code", length = 60, nullable = false)
    private String code;

    @Column(name = "total", precision = 38, scale = 2)
    private BigDecimal total;

    @Column(name = "customer_name", length = 300)
    private String customerName;

    @Column(name = "customer_mobile", length = 120)
    private String customerMobile;

    @Column(name = "customer_email", length = 120)
    private String customerEmail;

    @Column(name = "customer_address", length = 300)
    private String customerAddress;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "description", length = 500)
    private String description;

    //  Khóa ngoại: 1 Đơn hàng có 1 Trạng thái
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_status")
    private OrderStatus orderStatus;

    // Khóa ngoại: 1 Đơn hàng có Nhiều Sản phẩm (Chi tiết đơn)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "saleOrder")
    private Set<SaleOrderProduct> saleOrderProducts = new HashSet<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Set<SaleOrderProduct> getSaleOrderProducts() {
		return saleOrderProducts;
	}

	public void setSaleOrderProducts(Set<SaleOrderProduct> saleOrderProducts) {
		this.saleOrderProducts = saleOrderProducts;
	}
}
