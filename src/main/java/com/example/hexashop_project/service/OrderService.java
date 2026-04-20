package com.example.hexashop_project.service;

import com.example.hexashop_project.cart.Cart;
import com.example.hexashop_project.cart.CartItem;
import com.example.hexashop_project.cart.CartService;
import com.example.hexashop_project.model.*;
import com.example.hexashop_project.repository.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Transactional // Đảm bảo nếu một bước lỗi thì toàn bộ sẽ được rollback (không lưu gì hết)
    public SaleOrder placeOrder(SaleOrder orderData, HttpSession session) {
        // Lấy giỏ hàng hiện tại
        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống!");
        }

        // Thiết lập thông tin đơn hàng tổng quát
        // Tạo mã đơn hàng ngẫu nhiên (Vd: ORD-A1B2...)
        String orderCode = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        orderData.setCode(orderCode);
        orderData.setTotal(BigDecimal.valueOf(cart.getTotalPrice()));
        orderData.setCreateDate(new Date());
        orderData.setStatus(true);

        // Lấy trạng thái mặc định ban đầu là "Pending"
        OrderStatus pendingStatus = orderStatusRepository.findByName("Pending");
        orderData.setOrderStatus(pendingStatus);

        // Duyệt qua từng món trong giỏ để tạo SaleOrderProduct và trừ kho
        for (CartItem item : cart.getItems()) {
            SaleOrderProduct orderProduct = new SaleOrderProduct();
            orderProduct.setSaleOrder(orderData); // Khóa ngoại
            
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));
            
            // Truyền cả đối tượng Product vào
            orderProduct.setProduct(product);
            
            orderProduct.setName(item.getProductName());
            orderProduct.setQuantity(item.getQuantity());
            orderProduct.setPrice(BigDecimal.valueOf(item.getPrice()));
            
            // Lưu thông tin Phân loại vào mô tả
            orderProduct.setDescription("Color: " + item.getColor() + ", Size: " + item.getSize());
            orderProduct.setCreateDate(new Date());
            orderProduct.setStatus(true);

            orderData.getSaleOrderProducts().add(orderProduct);

            // TRỪ TỒN KHO
            updateProductStock(item);
        }

        // Lưu đơn hàng (Cascading sẽ tự lưu luôn SaleOrderProduct)
        SaleOrder savedOrder = saleOrderRepository.save(orderData);

        // Xóa giỏ hàng sau khi đặt thành công
        clearCart(session, cart);

        return savedOrder;
    }

    private void updateProductStock(CartItem item) {
        Product product = productRepository.findById(item.getProductId()).orElse(null);
        if (product != null) {
            for (ProductVariant variant : product.getVariants()) {
                if (variant.getColor().equals(item.getColor()) && variant.getSize().equals(item.getSize())) {
                    int newStock = variant.getStockQuantity() - item.getQuantity();
                    if (newStock < 0) throw new RuntimeException("Sản phẩm " + item.getProductName() + " đã hết hàng!");
                    variant.setStockQuantity(newStock);
                    break;
                }
            }
            productRepository.save(product);
        }
    }

    private void clearCart(HttpSession session, Cart cart) {
        // Nếu là giỏ hàng Session
        if (cart.getId() == null) {
            session.removeAttribute("myCart");
        } else {
            // Nếu là giỏ hàng Database, cần xóa các item trong DB
            cartService.clearCartItems(cart);
        }
    }
    
    @Transactional
    public String cancelOrder(String orderCode) {
        // Tìm đơn hàng theo mã
        SaleOrder order = saleOrderRepository.findByCode(orderCode);
        if (order == null) return "Order not found!";

        // Kiểm tra điều kiện: Chỉ được hủy khi đơn hàng đang ở trạng thái 'Pending'
        if (!order.getOrderStatus().getName().equals("Pending")) {
            return "Only 'Pending' orders can be cancelled!";
        }

        // Cập nhật trạng thái sang 'Cancelled'
        OrderStatus cancelledStatus = orderStatusRepository.findByName("Cancelled");
        order.setOrderStatus(cancelledStatus);
        order.setUpdateDate(new java.util.Date());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
        	order.setUpdateBy(auth.getName()); // Lưu username người thực hiện hủy 
        } else {
            order.setUpdateBy("Guest"); // Nếu là khách vãng lai
        }

        // HOÀN TRẢ TỒN KHO (Restock)
        for (SaleOrderProduct orderProduct : order.getSaleOrderProducts()) {
            restockProduct(orderProduct);
        }

        saleOrderRepository.save(order);
        return "SUCCESS";
    }

    private void restockProduct(SaleOrderProduct orderProduct) {
    	Product productInDb = productRepository.findById(orderProduct.getProduct().getId()).orElse(null);
    	
        if (productInDb != null) {
            // Phân tích thông tin Color/Size từ description (Ví dụ: "Color: Black, Size: M")
            String desc = orderProduct.getDescription();
            String color = desc.substring(desc.indexOf("Color: ") + 7, desc.indexOf(", Size:"));
            String size = desc.substring(desc.indexOf("Size: ") + 6);

            for (ProductVariant variant : productInDb.getVariants()) {
                if (variant.getColor().equals(color) && variant.getSize().equals(size)) {
                    // Cộng lại số lượng đã mua vào kho
                    variant.setStockQuantity(variant.getStockQuantity() + orderProduct.getQuantity());
                    break;
                }
            }
            productRepository.save(productInDb);
        }
    }
    
    @Transactional
    public void updateOrderStatus(Integer orderId, String statusName) {
        SaleOrder order = saleOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        OrderStatus newStatus = orderStatusRepository.findByName(statusName);
        if (newStatus == null) throw new RuntimeException("Trạng thái không hợp lệ!");

        order.setOrderStatus(newStatus);
        order.setUpdateDate(new java.util.Date());
        
        // Lấy tên admin đang đăng nhập để lưu vết
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        order.setUpdateBy(auth.getName());

        saleOrderRepository.save(order);
    }
}
