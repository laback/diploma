package com.by.gomel.gstu.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity implements UserDetails {

    @Column(name = "user_surname")
    private String surName;

    @Column(name = "user_name")
    private String firstName;

    @Column(name = "user_lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "login")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @Transient
    private String confirmPassword;

    @Transient
    private Long roleId;

    public User(User user){
        this(user.getSurName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRoleId());
    }

    public User(String surName, String firstName, String lastName, String email, Long roleId) {
        if(email == null){
            setCustomerData(surName, firstName, lastName, roleId);
        } else{
            setEmployeeData(surName, firstName, lastName, email, roleId);
        }
    }

    public User() {

    }

    public String getFio(){
        return String.format("%s %s %s", surName, firstName, lastName);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private void setCustomerData(String surName, String firstName, String lastName, Long roleId){
        this.surName = surName;
        this.firstName = firstName;
        this.lastName = lastName;

        role = new Role(roleId);

        setUpdated(LocalDateTime.now());
    }

    private void setEmployeeData(String surName, String firstName, String lastName, String email, Long roleId){
        setCustomerData(surName, firstName, lastName, roleId);

        this.email = email;

        this.username = firstName + "_" + surName;
        this.password = String.valueOf(this.username.hashCode());
    }

    public void appendUniqueNumber(long uniqueNumber){
        username += "_" + uniqueNumber;
    }
}
