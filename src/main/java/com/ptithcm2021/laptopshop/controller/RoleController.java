package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.RoleRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.RoleResponse;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.service.PermissionService;
import com.ptithcm2021.laptopshop.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request){
        return ApiResponse.<RoleResponse>builder().data(roleService.createRole(request)).build();
    }

    @PostMapping("/update")
    public ApiResponse<RoleResponse> updateRole(@RequestBody @Valid RoleRequest request){
        return ApiResponse.<RoleResponse>builder().data(roleService.updateRole(request)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable String id){
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().message("Delete role successful").build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String id){
        return ApiResponse.<RoleResponse>builder().data(roleService.getRole(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<RoleResponse>> getRoles(){
        return ApiResponse.<List<RoleResponse>>builder().data(roleService.getRoles()).build();
    }

    @GetMapping("/permissions")
    public ApiResponse<List<Permission>> getPermissions() {
        return ApiResponse.<List<Permission>>builder().data(permissionService.getPermissions()).build();
    }
}
