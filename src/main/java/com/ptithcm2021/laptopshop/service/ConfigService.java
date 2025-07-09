package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ConfigRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ConfigResponse;
import com.ptithcm2021.laptopshop.model.entity.Config;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ConfigService {
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ConfigResponse createConfig (ConfigRequest config);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void createConfig(ConfigRequest request, ProductDetail productDetail);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    ConfigResponse updateConfig (ConfigRequest request, long configId);

    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    void updateConfig (ConfigRequest request, Config config);

    void deleteConfig(long configId);
}
