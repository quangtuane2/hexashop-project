package com.example.hexashop_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class ContactDto {

	private Integer id;

	@NotEmpty(message = "Please enter your name")
	private String name;

	@NotEmpty(message = "Please enter your email")
	@Email(message = "Invalid email format")
	private String email;

	@NotEmpty(message = "Message cannot be empty")
	private String message;

	public ContactDto() {
		super();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
