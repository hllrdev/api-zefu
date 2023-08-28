package com.service.zefu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.zefu.enums.EnumRole;
import com.service.zefu.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    
    RoleModel findByRole(EnumRole role);

}
