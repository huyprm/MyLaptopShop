package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.enums.PermissionEnum;
import com.ptithcm2021.laptopshop.repository.PermissionRepository;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void initRoles() {
        if (roleRepository.count() > 0) {
            log.info("Roles already exist, skipping init.");
            return;
        }

        Permission fullControl = permissionRepository.findById(PermissionEnum.OWNER.name())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        Permission userPermission = permissionRepository.findById(PermissionEnum.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        List<Role> roles = List.of(
                Role.builder()
                        .id("ADMIN")
                        .description("ADMIN")
                        .permission(Set.of(fullControl))
                        .build(),
                Role.builder()
                        .id("CUSTOMER")
                        .description("CUSTOMER")
                        .permission(Set.of(userPermission))
                        .build()
        );

        roleRepository.saveAll(roles);
        log.info("Default roles ADMIN and CUSTOMER created.");
    }
}
