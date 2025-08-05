package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.OrderRequest;
import com.ptithcm2021.laptopshop.model.dto.response.Order.OrderListResponse;
import com.ptithcm2021.laptopshop.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "userId", source = "user.id")
    OrderListResponse toOrderListResponse(Order order);
}
