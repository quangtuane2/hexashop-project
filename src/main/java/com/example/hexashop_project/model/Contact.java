package com.example.hexashop_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_contact")
public class Contact extends BaseModel {

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Lob // Dùng cho kiểu TEXT trong database
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "description", length = 500)
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
}
