package com.example.hexashop_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDto {

	private Integer id;

	@NotEmpty(message = "Username is required")
	@Size(min = 4, message = "Username must be at least 4 characters")
	private String username;

	@NotEmpty(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	private String firstname;
	private String lastname;

	@NotEmpty(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	private String email;

	private String mobile;
	private String address;
	
	private Boolean status = Boolean.TRUE;

	public UserDto() {
		super();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getFirstname() { return firstname; }
	public void setFirstname(String firstname) { this.firstname = firstname; }

	public String getLastname() { return lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getMobile() { return mobile; }
	public void setMobile(String mobile) { this.mobile = mobile; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public Boolean getStatus() { return status; }
	public void setStatus(Boolean status) { this.status = status; }
}
