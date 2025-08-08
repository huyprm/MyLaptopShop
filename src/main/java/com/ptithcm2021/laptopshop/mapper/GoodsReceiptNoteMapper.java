package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteDetailResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteListResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoodsNoteReciept.GoodsReceiptNoteResponse;
import com.ptithcm2021.laptopshop.model.entity.GRNDetail;
import com.ptithcm2021.laptopshop.model.entity.GoodsReceiptNote;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GoodsReceiptNoteMapper {

    @Mapping(target = "staffName", source = "staff.fullName")
    @Mapping(target = "staffId", source = "goodsReceiptNote.staff.id")
    @Mapping(target = "purchaseOrderCode", source = "purchaseOrder.code")
    @Mapping(target = "grnDetails", source = "grnDetails", qualifiedByName = "toDetailResponseList")
    GoodsReceiptNoteResponse toResponse(GoodsReceiptNote goodsReceiptNote);

    @Mapping(target = "staffName", source = "staff.fullName")
    @Mapping(target = "staffId", source = "goodsReceiptNote.staff.id")
    @Mapping(target = "purchaseOrderCode", source = "purchaseOrder.code")
    GoodsReceiptNoteListResponse toListResponse(GoodsReceiptNote goodsReceiptNote);

//    @Mapping(target = "productTitle", source = "productDetail.title")
//    @Mapping(target = "serialNumbers", source = "serialNumber.serialNumber")
//    @Mapping(target = "thumbnail", source = "productDetail.thumbnail")
//    GoodsReceiptNoteDetailResponse toDetailResponse(GRNDetail grnDetail);

//    @Named("name")
//    default String getName(User user) {
//        return user.getFirstName() + " " + user.getLastName();
//    }

    @Named("toDetailResponseList")
    default List<GoodsReceiptNoteDetailResponse> toDetailResponseList(List<GRNDetail> grnDetails) {
        List<GoodsReceiptNoteDetailResponse> detailResponses = new ArrayList<>();

        if (grnDetails == null || grnDetails.isEmpty()) {
            return detailResponses;
        }

        List<String> serialNumbers = new ArrayList<>();
        int quantity = 0;
        Long currentProductId = grnDetails.get(0).getProductDetail().getId();
        GRNDetail currentDetail = grnDetails.get(0); // để lấy info thumbnail/title

        for (int i = 0; i < grnDetails.size(); i++) {
            GRNDetail detail = grnDetails.get(i);

            // Nếu cùng product
            if (detail.getProductDetail().getId().equals(currentProductId)) {
                serialNumbers.add(detail.getSerialNumber().getSerialNumber());
                quantity += detail.getQuantity();
            }

            // Nếu là dòng cuối hoặc sản phẩm đã khác
            boolean isLast = (i == grnDetails.size() - 1);
            boolean isNextDifferent = !isLast &&
                    !grnDetails.get(i + 1).getProductDetail().getId().equals(currentProductId);

            if (isLast || isNextDifferent) {
                detailResponses.add(GoodsReceiptNoteDetailResponse.builder()
                        .productTitle(currentDetail.getProductDetail().getTitle())
                        .thumbnail(currentDetail.getProductDetail().getThumbnail())
                        .serialNumbers(new ArrayList<>(serialNumbers))
                        .quantity(quantity)
                        .unitPrice(currentDetail.getUnitPrice())
                        .build());

                // Reset for next group
                serialNumbers.clear();
                quantity = 0;

                // Nếu không phải cuối cùng thì chuẩn bị cho sản phẩm tiếp theo
                if (!isLast) {
                    currentProductId = grnDetails.get(i + 1).getProductDetail().getId();
                    currentDetail = grnDetails.get(i + 1);
                }
            }
        }

        return detailResponses;
    }

}
