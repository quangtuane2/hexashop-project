package com.example.hexashop_project.dto;

public class ProductImageDto {

	private Integer id; // Dùng khi Update/Xóa ảnh cũ
    private String title;
    private String path; // Đường dẫn ảnh cũ hiện tại
	
    public ProductImageDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
    
}
