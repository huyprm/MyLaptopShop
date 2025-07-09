package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.entity.Permission;

import java.util.List;

public interface PermissionService {
    void initPermissions();

    List<Permission> getPermissions();
}
