package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.GoodsReceiptNoteDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsReceiptNoteResponse;
import com.ptithcm2021.laptopshop.model.entity.GRNDetail;
import com.ptithcm2021.laptopshop.model.entity.GoodsReceiptNote;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GoodsReceiptNoteMapper {

    @Mapping(target = "staffName", source = "staff", qualifiedByName = "name")
    @Mapping(target = "staffId", source = "goodsReceiptNote.staff.id")
    @Mapping(target = "purchaseOrderCode", source = "purchaseOrder.code")
    GoodsReceiptNoteResponse toResponse(GoodsReceiptNote goodsReceiptNote);

    @Mapping(target = "productTitle", source = "productDetail.title")
    @Mapping(target = "serialNumber", source = "serialNumber.serialNumber")
    GoodsReceiptNoteDetailResponse toDetailResponse(GRNDetail grnDetail);

    @Named("name")
    default String getName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
