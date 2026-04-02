package com.example.hexashop_project.controller.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import com.example.hexashop_project.dto.ProductDto;
import com.example.hexashop_project.model.Product;
import com.example.hexashop_project.service.CategoryService;
import com.example.hexashop_project.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // CategoryService để lấy danh sách danh mục đổ vào thẻ <select>
    @Autowired
    private CategoryService categoryService;

    // HIỂN THỊ DANH SÁCH (Có tìm kiếm và phân trang)
    @GetMapping({"", "/"})
    public String home(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false, defaultValue = "id") String column,
            @RequestParam(defaultValue = "desc") String direction, // Mặc định xếp mới nhất lên đầu
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryId) {

        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, column));

        Page<Product> pageData = productService.searchProducts(categoryId, name, pageable);

        model.addAttribute("productsPage", pageData);
        model.addAttribute("products", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("column", column);
        model.addAttribute("direction", direction);
        model.addAttribute("name", name);
        model.addAttribute("categoryId", categoryId);
        
        // Truyền danh sách Category ra View để làm bộ lọc tìm kiếm
        model.addAttribute("categories", categoryService.findAll());

        return "administrator/product/product";
    }

    // GIAO DIỆN THÊM MỚI SẢN PHẨM
    @GetMapping("/add")
    public String add(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        
        // Truyền danh sách Category ra View để chọn khi thêm SP
        model.addAttribute("categories", categoryService.findAll());
        
        return "administrator/product/add";
    }

    // XỬ LÝ LƯU SẢN PHẨM MỚI
    @PostMapping("/add/save")
    public String saveNewProduct(Model model, 
            @Valid @ModelAttribute ProductDto productDto, 
            BindingResult result) {
        
        // Kiểm tra trùng tên
        if (productService.existByName(productDto.getName())) {
            result.rejectValue("name", null, "Tên sản phẩm đã tồn tại, vui lòng chọn tên khác!");
        }

        // Bắt lỗi Validation (DTO)
        if (result.hasErrors()) {
            model.addAttribute("productDto", productDto);
            model.addAttribute("categories", categoryService.findAll());
            return "administrator/product/add";
        }

        try {
            // Gọi Service để lưu (Đã bao gồm xử lý upload ảnh trong Service)
            productService.insert(productDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("saveError", true);
            model.addAttribute("categories", categoryService.findAll());
            return "administrator/product/add";
        }

        return "redirect:/admin/product";
    }

    // GIAO DIỆN SỬA SẢN PHẨM
    @GetMapping("/edit")
    public String editProduct(Model model, @RequestParam Integer id, @RequestParam(defaultValue = "0") int page) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product";
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setIsHot(product.getIsHot());
        productDto.setShortDescription(product.getShortDescription());
        productDto.setDetailDescription(product.getDetailDescription());
        productDto.setStatus(product.getStatus());
        
        // Chuyền đường dẫn ảnh hiện tại ra View để hiển thị
        productDto.setAvatar(product.getAvatar());

        if (product.getCategory() != null) {
            productDto.setCategoryId(product.getCategory().getId());
        }

        model.addAttribute("productDto", productDto);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        
        model.addAttribute("currentPage", page);

        return "administrator/product/edit";
    }

    // XỬ LÝ CẬP NHẬT SẢN PHẨM
//    @PostMapping("/edit/save")
//    public String updateProduct(Model model, 
//            @Valid @ModelAttribute ProductDto productDto, 
//            @RequestParam Integer id, 
//            BindingResult result) {
//        
//        Product product = productService.findById(id);
//        if (product == null) return "redirect:/admin/product";
//
//        // Kiểm tra trùng tên với sản phẩm khác
//        if (!productDto.getName().equalsIgnoreCase(product.getName()) && productService.existByName(productDto.getName())) {
//            result.rejectValue("name", null, "Tên sản phẩm đã tồn tại!");
//        }
//
//        if (result.hasErrors()) {
//            productDto.setAvatar(product.getAvatar()); // Giữ lại đường dẫn ảnh khi có lỗi form
//            model.addAttribute("product", product);
//            model.addAttribute("productDto", productDto);
//            model.addAttribute("categories", categoryService.findAll());
//            return "administrator/product/edit";
//        }
//
//        try {
//            productService.update(id, productDto);
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("saveError", true);
//            return "administrator/product/edit";
//        }
//
//        return "redirect:/admin/product";
//    }
    
    @PostMapping("/edit-ajax/{id}")
    @ResponseBody
    public ResponseEntity<?> updateProductAjax(@PathVariable Integer id, @Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        
        Map<String, String> errors = new HashMap<>();

        // Kiểm tra lỗi Validation từ @Valid (Để trống, sai định dạng...)
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors); 
        }
    	
    	try {
            Product product = productService.findById(id);
            if (product == null) {
                return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
            }

            // Kiểm tra trùng tên với sản phẩm khác 
            if (!productDto.getName().equalsIgnoreCase(product.getName()) && productService.existByName(productDto.getName())) {
                return ResponseEntity.badRequest().body("Tên sản phẩm đã tồn tại!");
            }

            // Gọi Service để lưu cập nhật 
            productService.update(id, productDto);
            
            return ResponseEntity.ok("Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật dữ liệu: " + e.getMessage());
        }
    }

    // XÓA MỀM SẢN PHẨM
//    @GetMapping("/delete")
//    public String deleteProduct(@RequestParam Integer id) {
//        if (id != null && id > 0) {
//            productService.inactive(id);
//        }
//        return "redirect:/admin/product";
//    }
    
    @DeleteMapping("/delete-ajax/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProductAjax(@PathVariable Integer id) {
        try {
            if (id != null && id > 0) {
            	productService.inactive(id); 
                return ResponseEntity.ok("Xóa sản phẩm thành công!");
            } else {
                return ResponseEntity.badRequest().body("Không thể xóa sản phẩm này!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
