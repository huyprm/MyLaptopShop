package com.ptithcm2021.laptopshop.config;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.entity.LoginIdentifier;
import com.ptithcm2021.laptopshop.model.entity.Permission;
import com.ptithcm2021.laptopshop.model.entity.Role;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import com.ptithcm2021.laptopshop.model.enums.PermissionEnum;
import com.ptithcm2021.laptopshop.repository.LoginIdentifierRepository;
import com.ptithcm2021.laptopshop.repository.PermissionRepository;
import com.ptithcm2021.laptopshop.repository.RoleRepository;
import com.ptithcm2021.laptopshop.service.PermissionService;
import com.ptithcm2021.laptopshop.service.RoleService;
import com.ptithcm2021.laptopshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    private final UserService userService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @Bean
    ApplicationRunner initApplicationRunner() {
        return args -> {
            permissionService.initPermissions();
            roleService.initRoles();
            userService.initAdminUser();
        };
    }
}
