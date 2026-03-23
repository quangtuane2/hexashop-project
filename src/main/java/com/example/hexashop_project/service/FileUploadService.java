package com.example.hexashop_project.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hexashop_project.constant.HexaConstant;

@Service
public class FileUploadService {

	public String uploadFile(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}

		// Lấy tên file gốc (VD: ao-somi.jpg)
		String originalFilename = file.getOriginalFilename();
		
		// Để tránh trùng tên file khi upload nhiều lần, thường người ta sẽ gắn thêm System.currentTimeMillis()
		// VD: 16982348723_ao-somi.jpg
		String fileName = System.currentTimeMillis() + "_" + originalFilename;

		// Tạo đối tượng File trỏ tới thư mục lưu trữ
		File saveFile = new File(HexaConstant.FOLDER_UPLOAD + fileName);

		// Nếu thư mục UploadFiles chưa tồn tại thì tự động tạo mới
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		// Lưu file vật lý vào ổ cứng
		file.transferTo(saveFile);

		// Trả về đường dẫn ảo để lưu vào Database (Cột avatar)
		return "/UploadFiles/" + fileName;
	}
}
