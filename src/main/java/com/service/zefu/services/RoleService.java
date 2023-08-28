package com.service.zefu.services;

import org.springframework.stereotype.Service;

import com.service.zefu.enums.EnumRole;
import com.service.zefu.models.RoleModel;
import com.service.zefu.repositories.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class RoleService {
    
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleModel create(RoleModel role){
        return roleRepository.save(role);
    }
    
    public RoleModel getRole(EnumRole enumRole){
        return roleRepository.findByRole(enumRole);
    }

}
