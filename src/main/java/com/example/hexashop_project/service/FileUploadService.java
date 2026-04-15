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

		String originalFilename = file.getOriginalFilename();
		
		String fileName = System.currentTimeMillis() + "_" + originalFilename;

		File saveFile = new File(HexaConstant.FOLDER_UPLOAD + fileName);

		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		file.transferTo(saveFile);

		return "/UploadFiles/" + fileName;
	}
}
