package com.example.application.service;

import com.example.application.model.Role;
import com.example.application.repository.RoleRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    public void addRoleByName(String name){
        Role role = new Role(name);
        role.setUpdated(LocalDateTime.now());
        role.setCreated(LocalDateTime.now());
        roleRepository.save(role);
    }

    public Long getAdminRoleId(){
        return roleRepository.findRoleByName("ROLE_ADMIN").getId();
    }

    public Long getEmployeeRoleId(){
        return roleRepository.findRoleByName("ROLE_EMPLOYEE").getId();
    }

    public Long getCustomerRoleId(){
        return roleRepository.findRoleByName("ROLE_CUSTOMER").getId();
    }
}
