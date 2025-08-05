package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.entity.Permission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PermissionService {
    void initPermissions();

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PERMISSION')")
    List<Permission> getPermissions();
}
