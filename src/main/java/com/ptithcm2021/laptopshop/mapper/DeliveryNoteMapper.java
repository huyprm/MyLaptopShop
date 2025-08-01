package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.DeliveryNote.DeliveryNoteResponse;
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
    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
    DeliveryNoteDetailResponse toDeliveryNoteDetailResponse(DeliveryNoteDetail deliveryNoteDetail);

    @Mapping(target = "staffName", source = "staff", qualifiedByName = "name")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "orderCode", source = "order.code")
    DeliveryNoteListResponse toDeliveryNoteListResponse(DeliveryNote deliveryNote);

    @Named("name")
    default String getName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
