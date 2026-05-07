package com.example.hexashop_project.controller.administrator;

import com.example.hexashop_project.model.User;
import com.example.hexashop_project.repository.RoleRepository;
import com.example.hexashop_project.repository.SaleOrderRepository;
import com.example.hexashop_project.repository.UserRepository;
import com.example.hexashop_project.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/customers")
public class AdminCustomerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private FileUploadService fileUploadService;

    // DANH SÁCH KHÁCH HÀNG (có tìm kiếm + phân trang)
    @GetMapping({ "", "/" })
    public String listCustomers(
            Model model,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));

        Page<User> pageData;
        if (keyword == null || keyword.isBlank()) {
            pageData = userRepository.findAll(pageable);
        } else {
            pageData = userRepository.searchByKeyword(keyword, pageable);
        }

        model.addAttribute("customers", pageData.getContent());
        model.addAttribute("customersPage", pageData);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("totalElements", pageData.getTotalElements());

        return "administrator/customer/customer-list";
    }

    // TRANG XEM CHI TIẾT / CHỈNH SỬA THÔNG TIN CUSTOMER
    @GetMapping("/detail")
    public String viewCustomerDetail(@RequestParam Integer id, Model model) {
        User customer = userRepository.findById(id).orElse(null);
        if (customer == null) {
            return "redirect:/admin/customers";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("roles", roleRepository.findAll());

        // Tải đơn hàng theo email của khách hàng
        if (customer.getEmail() != null && !customer.getEmail().isBlank()) {
            model.addAttribute("orders",
                saleOrderRepository.findByCustomerEmailOrderByCreateDateDesc(customer.getEmail()));
        }
        return "administrator/customer/customer-detail";
    }

    // XỬ LÝ CẬP NHẬT THÔNG TIN CUSTOMER
    @PostMapping("/update")
    public String updateCustomer(
            @RequestParam Integer id,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String description,
            RedirectAttributes ra) {

        User customer = userRepository.findById(id).orElse(null);
        if (customer == null) {
            ra.addFlashAttribute("errorMsg", "Không tìm thấy khách hàng!");
            return "redirect:/admin/customers";
        }

        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        customer.setEmail(email);
        customer.setMobile(mobile);
        customer.setAddress(address);
        customer.setDescription(description);
        userRepository.save(customer);

        ra.addFlashAttribute("successMsg", "Cập nhật thông tin khách hàng thành công!");
        return "redirect:/admin/customers/detail?id=" + id;
    }

    // BẬT / TẮT TRẠNG THÁI TÀI KHOẢN (AJAX)
    @PostMapping("/toggle-status/{id}")
    @ResponseBody
    public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {
        User customer = userRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng!");
        }
        Boolean current = customer.getStatus();
        customer.setStatus(current == null || !current);
        userRepository.save(customer);
        return ResponseEntity.ok(customer.getStatus() ? "active" : "inactive");
    }

    // XÓA CUSTOMER (AJAX)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Không tìm thấy khách hàng!");
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok("Xóa khách hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // CẬP NHẬT AVATAR CUSTOMER (AJAX)
    @PostMapping("/update-avatar/{id}")
    @ResponseBody
    public ResponseEntity<String> updateAvatar(
            @PathVariable Integer id,
            @RequestParam("avatarFile") MultipartFile avatarFile) {

        User customer = userRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy khách hàng!");
        }

        if (avatarFile == null || avatarFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng chọn file ảnh!");
        }

        // Kiểm tra định dạng file
        String contentType = avatarFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("File không hợp lệ, chỉ chấp nhận ảnh!");
        }

        try {
            String avatarPath = fileUploadService.uploadFile(avatarFile);
            customer.setAvatar(avatarPath);
            userRepository.save(customer);
            return ResponseEntity.ok(avatarPath); // Trả về đường dẫn ảnh mới
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi upload: " + e.getMessage());
        }
    }
}
