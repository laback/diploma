package com.by.gomel.gstu.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "role_name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @Transient
    private List<User> users;

    public Role(Long id, String name){
        setId(id);
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Role() {

    }

    public Role(Long roleId) {
        setId(roleId);
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
