package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Product.ConfigRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Product.ConfigResponse;
import com.ptithcm2021.laptopshop.model.entity.Config;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ConfigService {
    //@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
    ConfigResponse createConfig (ConfigRequest config);

    //@PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
    ConfigResponse createConfig(ConfigRequest request, ProductDetail productDetail);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
    ConfigResponse updateConfig (ConfigRequest request, long configId);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
    void updateConfig (ConfigRequest request, Config config);

    @PreAuthorize("hasAnyAuthority('SCOPE_OWNER', 'SCOPE_PERM_PRODUCT')")
    void deleteConfig(long configId);
}
