package com.example.hexashop_project.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_role")
public class Role extends BaseModel {

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 500, nullable = true)
    private String description;

    // Mối quan hệ Nhiều-Nhiều (Nhiều Role thuộc về Nhiều User) 
    // mappedBy chỉ ra rằng User mới là chủ thể quản lý mối quan hệ này
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}
