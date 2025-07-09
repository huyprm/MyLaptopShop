package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.RoleRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RoleResponse;
import com.ptithcm2021.laptopshop.model.entity.Role;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoleService {
    void initRoles();

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    RoleResponse createRole(RoleRequest role);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    RoleResponse updateRole(RoleRequest role);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void deleteRole(String id);

    RoleResponse getRole(String id);
    List<RoleResponse> getRoles();
}
