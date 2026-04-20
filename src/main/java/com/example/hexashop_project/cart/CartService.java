package com.example.hexashop_project.cart;

import com.example.hexashop_project.model.User;
import com.example.hexashop_project.repository.CartItemRepository;
import com.example.hexashop_project.repository.CartRepository;
import com.example.hexashop_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class CartService {
	
	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public void clearCartItems(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    // HÀM KIỂM TRA XEM AI ĐANG ĐĂNG NHẬP
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra xem có người đăng nhập không (và người đó KHÔNG PHẢI là khách ẩn danh)
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName(); // Lấy email/username của khách
            return userRepository.findByUsername(username); // Chui vào DB lấy nguyên đối tượng User ra
        }
        return null; // Trả về null nếu là khách vãng lai
    }
    
    // LẤY GIỎ HÀNG (KÈM CHỨC NĂNG GOM GIỎ HÀNG)
    public Cart getCart(HttpSession session) {
        User user = getLoggedInUser();

        if (user != null) {
            // LÀ KHÁCH ĐÃ ĐĂNG NHẬP (Lưu DB)
            Cart dbCart = cartRepository.findByUser(user);
            
            // Nếu khách này chưa có giỏ hàng trong DB thì tạo mới cho họ
            if (dbCart == null) {
                dbCart = new Cart();
                dbCart.setUser(user);
                dbCart = cartRepository.save(dbCart);
            }

            // CHỨC NĂNG MERGE CART (Gom giỏ hàng)
            // Kiểm tra xem trước khi login, khách có để đồ ở Session không?
            Cart sessionCart = (Cart) session.getAttribute("myCart");
            if (sessionCart != null && !sessionCart.getItems().isEmpty()) {
                mergeCart(sessionCart, dbCart);
                session.removeAttribute("myCart"); // Gom xong thì xóa Session đi cho nhẹ máy chủ
            }

            return dbCart;

        } else {
            // LÀ KHÁCH VÃNG LAI (Lưu Session) 
            Cart sessionCart = (Cart) session.getAttribute("myCart");
            if (sessionCart == null) {
                sessionCart = new Cart();
                session.setAttribute("myCart", sessionCart);
            }
            return sessionCart;
        }
    }
    
    // Chuyển đồ từ Session sang DB
    private void mergeCart(Cart sessionCart, Cart dbCart) {
        for (CartItem sessionItem : sessionCart.getItems()) {
            boolean isExist = false;
            // Quét xem trong DB có món này chưa
            for (CartItem dbItem : dbCart.getItems()) {
                if (dbItem.getProductId().equals(sessionItem.getProductId()) &&
                    dbItem.getColor().equals(sessionItem.getColor()) &&
                    dbItem.getSize().equals(sessionItem.getSize())) {

                    // Nếu có rồi thì cộng dồn số lượng
                    dbItem.setQuantity(dbItem.getQuantity() + sessionItem.getQuantity());
                    isExist = true;
                    break;
                }
            }
            // Nếu món này DB chưa có, bê nguyên từ Session bỏ vào
            if (!isExist) {
                sessionItem.setCart(dbCart); // Khai báo: Món hàng này thuộc về cái giỏ DB này
                dbCart.getItems().add(sessionItem);
            }
        }
        cartRepository.save(dbCart); // Lưu toàn bộ thay đổi xuống Database
    }
    
    // HÀM CHỨC NĂNG: THÊM / CẬP NHẬT / XÓA SẢN PHẨM

    // Thêm đồ vào giỏ
    public void addItem(HttpSession session, CartItem newItem) {
        Cart cart = getCart(session);
        boolean isExist = false;

        // Quét xem món này (Cùng ID, cùng Màu, cùng Size) đã có trong giỏ chưa?
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(newItem.getProductId()) &&
                item.getColor().equals(newItem.getColor()) &&
                item.getSize().equals(newItem.getSize())) {
                
                // Nếu có rồi thì chỉ tăng số lượng lên thôi
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                isExist = true;
                break;
            }
        }

        // Nếu là đồ mới , thì quăng thẳng vào giỏ
        if (!isExist) {
        	newItem.setCart(cart); // Bắt buộc phải có để Hibernate biết nó thuộc giỏ nào
            cart.getItems().add(newItem);
        }
        
        // NẾU GIỎ HÀNG CÓ ID (Tức là giỏ hàng DB), THÌ PHẢI LƯU LẠI VÀO DB
        if (cart.getId() != null) {
            cartRepository.save(cart);
        }
    }
    
    // Cập nhật số lượng
    public void updateQuantity(HttpSession session, Integer productId, String color, String size, int newQuantity) {
        Cart cart = getCart(session);
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId) && item.getColor().equals(color) && item.getSize().equals(size)) {
                item.setQuantity(newQuantity);
                break;
            }
        }
        if (cart.getId() != null) cartRepository.save(cart);
    }

    // Xóa món hàng
    public void removeItem(HttpSession session, Integer productId, String color, String size) {
        Cart cart = getCart(session);
        cart.getItems().removeIf(item -> 
            item.getProductId().equals(productId) && 
            item.getColor().equals(color) && 
            item.getSize().equals(size)
        );
        if (cart.getId() != null) cartRepository.save(cart);
    }
}
