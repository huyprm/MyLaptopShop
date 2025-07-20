package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.RoleMapper;
import com.ptithcm2021.laptopshop.model.dto.request.RoleRequest;
import com.ptithcm2021.laptopshop.model.dto.response.RoleResponse;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.enums.PermissionEnum;
import com.ptithcm2021.laptopshop.repository.PermissionRepository;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

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

    @Override
    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsById(request.getId())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));

        Role role = Role.builder()
                .id(request.getId())
                .description(request.getDescription())
                .color(request.getColor())
                .permission(permissions)
                .build();
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(RoleRequest request) {
        Role role = roleRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissionIds()));

        role.setColor(request.getColor());
        role.setPermission(permissions);
        role.setDescription(request.getDescription());
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public void deleteRole(String id) {
        if(!roleRepository.existsById(id)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        try{
            roleRepository.deleteById(id);
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public RoleResponse getRole(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }
}
