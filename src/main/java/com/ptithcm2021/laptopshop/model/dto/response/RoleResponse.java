package com.ptithcm2021.laptopshop.model.dto.response;

import com.ptithcm2021.laptopshop.model.dto.request.RoleRequest;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private String id;
    private String description;
    private Set<Permission> permission;
    private String color;
}
