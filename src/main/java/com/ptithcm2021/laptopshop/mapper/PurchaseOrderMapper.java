package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PurchaseOrder.PurchaseOrderResponse;
import com.ptithcm2021.laptopshop.model.entity.PurchaseOrder;
import com.ptithcm2021.laptopshop.model.entity.PurchaseOrderDetail;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "userOrderName", source = "userOrder", qualifiedByName = "username")
    @Mapping(target = "userId", source = "userOrder.id")
    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder purchaseOrder);

    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "userOrderName", source = "userOrder", qualifiedByName = "username")
    @Mapping(target = "userId", source = "userOrder.id")
    PurchaseOrderListResponse toPurchaseOrderListResponse(PurchaseOrder purchaseOrder);

    @Mapping(target ="title", source = "productDetail.title")
    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
    @Mapping(target = "productDetailId", source = "productDetail.id")
    PurchaseOrderDetailResponse toPurchaseOrderDetailResponse(PurchaseOrderDetail purchaseOrderDetail);

    @Named("username")
    default String mapUsername(User user) {
        return user.getFirstName()+" "+user.getLastName();
    }
}
