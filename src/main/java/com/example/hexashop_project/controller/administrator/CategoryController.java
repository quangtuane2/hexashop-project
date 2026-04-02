package com.example.hexashop_project.controller.administrator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import com.example.hexashop_project.dto.CategoryDto;
import com.example.hexashop_project.model.Category;
import com.example.hexashop_project.service.CategoryService;
import com.example.hexashop_project.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
	
	@Autowired
	private CategoryService cs;
	
	@Autowired
	private UserService us;
	
	// Sắp xếp + Phân trang + Tìm kiếm
	@GetMapping({"/", ""})
	public String home(Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int size,
			@RequestParam(required = false, defaultValue = "id") String column,
			@RequestParam(defaultValue = "asc") String direction,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
		
		// Xử lý Sort
		Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, column));
		
		// Xử lý Search Date
		LocalDateTime fromInclusive = fromDate != null ? fromDate.atStartOfDay() : null;
		LocalDateTime toInclusive = toDate != null ? toDate.atTime(LocalTime.MAX) : null;
		
		Page<Category> pageData = cs.findAllSortPageableAndSearch(name, fromInclusive, toInclusive, pageable);
		
		model.addAttribute("categoriesPage", pageData);
		model.addAttribute("categories", pageData.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("column", column);
		model.addAttribute("direction", direction);
		model.addAttribute("name", name);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		
		return "administrator/category/category";
	}

	@GetMapping("/add")
	public String add(Model model) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCreateDate(LocalDate.now());
		
		model.addAttribute("categoryDto", categoryDto);
		model.addAttribute("categories", cs.findAll());
		model.addAttribute("users", us.findAll());
		
		return "administrator/category/add";
	}
	
	@PostMapping("/add/save")
	public String saveNewCategory(Model model, @Valid @ModelAttribute CategoryDto categoryDto, BindingResult result) {
		if(cs.existByName(categoryDto.getName())) {
			result.rejectValue("name", null, "Tên danh mục đã tồn tại, xin chọn tên khác");
		}
		
		if(result.hasErrors()) {
			categoryDto.setCreateDate(LocalDate.now());
			model.addAttribute("categoryDto", categoryDto);
			model.addAttribute("categories", cs.findAll());
			model.addAttribute("users", us.findAll());
			return "administrator/category/add";
		}
		
		try {
			cs.insert(categoryDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("saveError", true);
			return "administrator/category/add";
		}
		
		return "redirect:/admin/category";
	}
	
	@GetMapping("/edit")
	public String editCategory(Model model, @RequestParam Integer id, @RequestParam(defaultValue = "0") int page) {
		Category category = cs.findById(id);
		if(category == null) return "redirect:/admin/category";
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName(category.getName());
		categoryDto.setDescription(category.getDescription());
		categoryDto.setStatus(category.getStatus());
		if(category.getParentCategory() != null) {
			categoryDto.setCategoryId(category.getParentCategory().getId());
		}
		
		model.addAttribute("categoryDto", categoryDto);
		model.addAttribute("category", category);
		model.addAttribute("categories", cs.findAll());
		model.addAttribute("users", us.findAll());
		
		model.addAttribute("currentPage", page);
		
		return "administrator/category/edit";
	}
	
//	@PostMapping("/edit/save")
//	public String updateCategory(Model model, @Valid @ModelAttribute CategoryDto categoryDto, @RequestParam Integer id, BindingResult result) {
//		Category category = cs.findById(id);
//		if(category == null) return "redirect:/admin/category";
//		
//		if(categoryDto.getCategoryId() != null && categoryDto.getCategoryId().equals(id)) {
//			result.rejectValue("categoryId", null, "Danh mục cha không thể là chính nó");
//		}
//		
//		if(!categoryDto.getName().equalsIgnoreCase(category.getName()) && cs.existByName(categoryDto.getName())){
//			result.rejectValue("name", null, "Tên danh mục đã tồn tại");
//		}
//		
//		if(result.hasErrors()) {
//			model.addAttribute("category", category);
//			model.addAttribute("categoryDto", categoryDto);
//			model.addAttribute("categories", cs.findAll());
//			model.addAttribute("users", us.findAll());
//			return "administrator/category/edit";
//		}
//		
//		try {
//			cs.update(id, categoryDto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("saveError", true);
//			return "administrator/category/edit";
//		}
//		
//		return "redirect:/admin/category";
//	}
	
	@PostMapping("/edit-ajax/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCategoryAjax(@PathVariable Integer id, 
            @Valid @ModelAttribute CategoryDto categoryDto, 
            BindingResult result) {
        
        Map<String, String> errors = new HashMap<>();

        // Validate form 
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors); 
        }

        try {
            Category currentCat = cs.findById(id); 
            if (currentCat == null) {
                return ResponseEntity.badRequest().body("Danh mục không tồn tại!"); 
            }

            // Validate logic: Kiểm tra trùng tên danh mục
            if (!categoryDto.getName().equalsIgnoreCase(currentCat.getName()) && cs.existByName(categoryDto.getName())) {
                errors.put("name", "Tên danh mục đã tồn tại trong hệ thống!");
                return ResponseEntity.badRequest().body(errors);
            }

            // Lưu vào DB
            cs.update(id, categoryDto); 
            return ResponseEntity.ok("Cập nhật danh mục thành công!");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật dữ liệu: " + e.getMessage());
        }
    }
	
//	@GetMapping("/delete")
//	public String deleteCategory(@RequestParam Integer id) {
//		if(id != null && id > 0) {
//			cs.inactive(id);
//		}
//		return "redirect:/admin/category";
//	}
	
	@DeleteMapping("/delete-ajax/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCategoryAjax(@PathVariable Integer id) {
        try {
            if (id != null && id > 0) {
                cs.inactive(id); 
                return ResponseEntity.ok("Xóa danh mục thành công!");
            } else {
                return ResponseEntity.badRequest().body("Không thể xóa danh mục này (có thể đang chứa sản phẩm)!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
