package com.example.hexashop_project.controller.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.model.ProductImage;
import com.example.hexashop_project.model.ProductVariant;
import com.example.hexashop_project.model.SaleOrder;
import com.example.hexashop_project.service.OrderService;
import com.example.hexashop_project.service.ProductService;
import com.example.hexashop_project.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import jakarta.servlet.http.HttpSession;
import com.example.hexashop_project.cart.Cart;
import com.example.hexashop_project.cart.CartService;
import com.example.hexashop_project.dto.UserRegisterDTO;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private com.example.hexashop_project.repository.SaleOrderRepository saleOrderRepository;

    @ModelAttribute("cart")
    public Cart populateCart(HttpSession session) {
        return cartService.getCart(session);
    }

    // Ánh xạ URL trang chủ (http://localhost:9090/ hoặc
    // http://localhost:9090/index)
    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        List<Product> menLatest = productService.getProductsByCategory(1, org.springframework.data.domain.PageRequest.of(0, 8, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"))).getContent();
        List<Product> womenLatest = productService.getProductsByCategory(2, org.springframework.data.domain.PageRequest.of(0, 8, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"))).getContent();
        List<Product> kidLatest = productService.getProductsByCategory(3, org.springframework.data.domain.PageRequest.of(0, 8, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"))).getContent();

        model.addAttribute("menLatest", menLatest);
        model.addAttribute("womenLatest", womenLatest);
        model.addAttribute("kidLatest", kidLatest);
        
        // Trỏ tới file: src/main/resources/templates/customer/index.html
        return "customer/index";
    }

    @GetMapping("/men")
    public String men(Model model, @RequestParam(defaultValue = "0") int page) {
        // Lấy tất cả sản phẩm Lastest và casual
        Page<Product> productPage = productService.getProductsByCategory(1,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        Page<Product> casualProducts = productService.getProductsByCategory(13,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));

        model.addAttribute("productPage", productPage);
        model.addAttribute("casualProducts", casualProducts);

        // Flash Sale (Lấy 8 sản phẩm có giảm giá)
        List<Product> flashSales = productService.getFlashSaleProducts(1, 8);

        // Featured (Lấy 8 sản phẩm đánh dấu Hot)
        List<Product> featuredProducts = productService.getFeaturedProducts(1, 8);

        // Truyền dữ liệu ra View
        model.addAttribute("flashSales", flashSales);
        model.addAttribute("featuredProducts", featuredProducts);

        return "customer/men";
    }

    // Chỉ trả về HTML của phần Latest
    @GetMapping("/men/latest-ajax")
    public String getLatestMenAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productPage = productService.getProductsByCategory(1,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("productPage", productPage);

        return "customer/men :: latestFragment";
    }

    // Chỉ trả về HTML của phần Casual
    @GetMapping("/men/casual-ajax")
    public String getCasualMenAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> casualProducts = productService.getProductsByCategory(13,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("casualProducts", casualProducts);

        return "customer/men :: casualFragment";
    }

    @GetMapping("/women")
    public String women(Model model, @RequestParam(defaultValue = "0") int page) {
        // Women's Latest & Casual (Lấy ID = 31 là Áo thun Nữ đồ mặc thường ngày)
        Page<Product> productPage = productService.getProductsByCategory(2,
                PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id")));
        Page<Product> casualProducts = productService.getProductsByCategory(31,
                PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id")));

        model.addAttribute("productPage", productPage);
        model.addAttribute("casualProducts", casualProducts);

        // Flash Sale (Lấy 8 sản phẩm Đồ Nữ có giảm giá)
        List<Product> flashSales = productService.getFlashSaleProducts(2, 8);

        // Featured (Lấy 8 sản phẩm Đồ Nữ đánh dấu Hot)
        List<Product> featuredProducts = productService.getFeaturedProducts(2, 8);

        model.addAttribute("flashSales", flashSales);
        model.addAttribute("featuredProducts", featuredProducts);

        return "customer/women";
    }

    // Chỉ trả về HTML của phần Latest
    @GetMapping("/women/latest-ajax")
    public String getLatestWomenAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productPage = productService.getProductsByCategory(2,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("productPage", productPage);

        // Chỉ trả về đoạn HTML có tên fragment là 'latestFragment' trong file
        // women.html
        return "customer/women :: latestFragment";
    }

    // Chỉ trả về HTML của phần Casual
    @GetMapping("/women/casual-ajax")
    public String getCasualWomenAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> casualProducts = productService.getProductsByCategory(31,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("casualProducts", casualProducts);

        return "customer/women :: casualFragment";
    }

    @GetMapping("/kids")
    public String kids(Model model, @RequestParam(defaultValue = "0") int page) {
        // Kids Latest & Casual
        Page<Product> productPage = productService.getProductsByCategory(3,
                PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id")));
        Page<Product> casualProducts = productService.getProductsByCategory(18,
                PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "id")));

        model.addAttribute("productPage", productPage);
        model.addAttribute("casualProducts", casualProducts);

        List<Product> flashSales = productService.getFlashSaleProducts(3, 8);

        List<Product> featuredProducts = productService.getFeaturedProducts(3, 8);

        model.addAttribute("flashSales", flashSales);
        model.addAttribute("featuredProducts", featuredProducts);

        return "customer/kids";
    }

    @GetMapping("/kids/latest-ajax")
    public String getLatestKidsAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productPage = productService.getProductsByCategory(3,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("productPage", productPage);

        return "customer/Kids :: latestFragment";
    }

    @GetMapping("/kids/casual-ajax")
    public String getCasualKidsAjax(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> casualProducts = productService.getProductsByCategory(18,
                PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("casualProducts", casualProducts);

        return "customer/Kids :: casualFragment";
    }

    @GetMapping("/single-product")
    public String singleProduct(@RequestParam Integer id, Model model) {
        Product product = productService.findById(id);

        if (product == null) {
            return "redirect:/";
        }

        List<String> imageUrls = new ArrayList<>();
        if (product.getAvatar() != null) {
            imageUrls.add(product.getAvatar());
        }
        if (product.getProductImages() != null) {
            for (ProductImage img : product.getProductImages()) {
                imageUrls.add(img.getPath());
            }
        }
        if (imageUrls.isEmpty()) {
            imageUrls.add("/assets/images/single-product-01.jpg");
        }
        model.addAttribute("productImages", imageUrls);

        List<String> colors = product.getVariants().stream()
                .map(ProductVariant::getColor)
                .distinct()
                .collect(Collectors.toList());

        List<String> sizes = product.getVariants().stream()
                .map(ProductVariant::getSize)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("colors", colors);
        model.addAttribute("sizes", sizes);

        // Lấy các sản phẩm liên quan
        Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> relatedProducts = productService.getProductsByCategory(product.getCategory().getId(), pageable);

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts.getContent());

        return "customer/single-product";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        return "customer/cart";
    }

    @GetMapping("/about")
    public String about() {
        return "customer/aboutUs";
    }

    @GetMapping("/contact")
    public String contact() {
        return "customer/contact";
    }

    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @GetMapping("/register")
    public String register() {
        return "customer/register";
    }
    
    // HÀM XỬ LÝ LƯU ĐĂNG KÝ
    @PostMapping("/register")
    public String processRegister(@ModelAttribute UserRegisterDTO dto, Model model, RedirectAttributes redirectAttributes) {
    	String result = userService.registerUser(dto);
        
        if ("SUCCESS".equals(result)) {
            // Đăng ký thành công → chuyển về trang thông báo "Kiểm tra email"
            redirectAttributes.addFlashAttribute("registeredEmail", dto.getEmail());
            return "redirect:/register-success";
        } else {
            // Nếu lỗi (trùng email, sai pass), ở lại trang đăng ký và báo lỗi
            model.addAttribute("errorMsg", result);
            return "customer/register";
        }
    }

    // TRANG THÔNG BÁO SAU ĐĂNG KÝ
    @GetMapping("/register-success")
    public String registerSuccess() {
        return "customer/register-success";
    }

    // XÁC MINH EMAIL — User click link trong email
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, RedirectAttributes redirectAttributes) {
        String result = userService.verifyEmail(token);
        switch (result) {
            case "SUCCESS":
                redirectAttributes.addFlashAttribute("successMsg",
                    "🎉 Email xác minh thành công! Tài khoản của bạn đã được kích hoạt. Vui lòng đăng nhập.");
                break;
            case "USED":
                redirectAttributes.addFlashAttribute("errorMsg",
                    "Link xác minh này đã được sử dụng rồi.");
                break;
            case "EXPIRED":
                redirectAttributes.addFlashAttribute("errorMsg",
                    "Link xác minh đã hết hạn. Vui lòng đăng ký lại.");
                break;
            default:
                redirectAttributes.addFlashAttribute("errorMsg",
                    "Link xác minh không hợp lệ.");
        }
        return "redirect:/login";
    }

    @GetMapping("/order-tracking")
    public String orderTracking(Model model, java.security.Principal principal) {
        if (principal != null) {
            // User đã đăng nhập: tự động load toàn bộ đơn hàng theo email tài khoản
            String email = principal.getName();
            List<SaleOrder> orders = saleOrderRepository.findByCustomerEmailOrderByCreateDateDesc(email);
            model.addAttribute("orders", orders);
            model.addAttribute("searchEmail", email);
        }
        return "customer/order-tracking";
    }



    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        
        // Nếu giỏ hàng trống thì không cho thanh toán, quay lại giỏ
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cart", cart);
        return "customer/checkout";
    }
    
    @PostMapping("/checkout")
    public String processCheckout(@ModelAttribute SaleOrder orderData, @RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            SaleOrder successOrder = orderService.placeOrder(orderData, session);
            
            // Nếu khách chọn thanh toán online (MOMO, VNPAY, BANK) -> chuyển sang trang hiển thị QR
            if (!"COD".equalsIgnoreCase(paymentMethod)) {
                return "redirect:/payment-qrcode?orderCode=" + successOrder.getCode();
            }
            
            redirectAttributes.addFlashAttribute("order", successOrder);
            redirectAttributes.addFlashAttribute("successMsg", "Congratulations! Your order has been placed successfully.");
            
            return "redirect:/order-tracking";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/payment-qrcode")
    public String paymentQrCode(@RequestParam("orderCode") String orderCode, Model model) {
        SaleOrder order = saleOrderRepository.findByCode(orderCode);
        if (order == null) {
            return "redirect:/order-tracking";
        }
        
        // Pass order details to the view
        model.addAttribute("order", order);
        
        return "customer/payment-qrcode";
    }

}
