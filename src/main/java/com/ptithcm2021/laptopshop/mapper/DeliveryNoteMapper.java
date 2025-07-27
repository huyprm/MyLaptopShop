package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNoteDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNoteResponse;
import com.ptithcm2021.laptopshop.model.entity.DeliveryNote;
import com.ptithcm2021.laptopshop.model.entity.DeliveryNoteDetail;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DeliveryNoteMapper {

    @Mapping(target = "staffName", source = "staff", qualifiedByName = "name")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "orderCode", source = "order.code")
    DeliveryNoteResponse toDeliveryNoteResponse(DeliveryNote deliveryNote);

    @Mapping(target = "productTitle", source = "productDetail.title")
    @Mapping(target = "serialNumber", source = "serialNumber.serialNumber")
    DeliveryNoteDetailResponse toDeliveryNoteDetailResponse(DeliveryNoteDetail deliveryNoteDetail);

    @Named("name")
    default String getName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
