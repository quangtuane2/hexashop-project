package com.example.hexashop_project.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.hexashop_project.constant.HexaConstant;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer, HexaConstant{
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/UploadFiles/**").addResourceLocations(
				"file:" + FOLDER_UPLOAD);
	}

}
