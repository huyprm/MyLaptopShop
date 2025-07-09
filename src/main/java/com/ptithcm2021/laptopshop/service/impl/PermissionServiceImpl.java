package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.model.enums.PermissionEnum;
import com.ptithcm2021.laptopshop.repository.PermissionRepository;
import com.ptithcm2021.laptopshop.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public void initPermissions() {
        Set<String> existing = permissionRepository.findAll()
                .stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        for (PermissionEnum e : PermissionEnum.values()) {
            if (!existing.contains(e.name())) {
                Permission permission = Permission.builder()
                        .id(e.name())
                        .description(e.name())
                        .build();
                permissionRepository.save(permission);
                log.info("Permission saved: {}", e.name());
            }
        }
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }
}
