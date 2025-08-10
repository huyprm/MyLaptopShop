package com.ptithcm2021.laptopshop.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    UNAUTHORIZED (1000, "Unauthorize", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN (1001, "Token invalid", HttpStatus.UNAUTHORIZED),
    REVOKED_TOKEN (1002, "Revoked Token", HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_FOUND (1003, "Username does not exists", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD (1004, "Wrong password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND (1005, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED(1006,"Username already exists" , HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(1007, "Role does not exist", HttpStatus.NOT_FOUND ),
    PERMISSION_NOT_FOUND(1008, "Permission does not exist", HttpStatus.NOT_FOUND),
    IDENTIFIER_UNDETERMINED(1009, "Identifier undetermined", HttpStatus.BAD_REQUEST),
    OTP_INCORRECT(1010,"OTP incorrect" , HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1011,"Role already exists" , HttpStatus.CONFLICT ),
    CANNOT_DELETE(1012,"Cannot delete" , HttpStatus.CONFLICT ),
    CATEGORY_NOT_FOUND(1013,"Category does not exist" , HttpStatus.NOT_FOUND ),
    COLOR_NOT_FOUND(1014,"Color does not exist" , HttpStatus.NOT_FOUND ),
    COLOR_NAME_EXISTED(1015,"color name already existed" , HttpStatus.CONFLICT ),
    COLOR_HEX_EXISTED(1016,"color hex already existed" , HttpStatus.CONFLICT ),
    BRAND_NOT_FOUND(1017,"Brand does not exist" , HttpStatus.NOT_FOUND),
    SERIES_NOT_FOUND(1018, "Series does not exist" , HttpStatus.NOT_FOUND ),
    PRODUCT_NOT_FOUND(1019,"Product does not exist" , HttpStatus.NOT_FOUND ),
    PRODUCT_ID_NULL(1020,"Product id null" , HttpStatus.BAD_REQUEST ),
    PRODUCT_DETAIL_ID_NULL(1021,"Product detail id null" , HttpStatus.BAD_REQUEST ),
    CONFIG_NOT_FOUND(1022, "Config does not exist" , HttpStatus.NOT_FOUND ),
    BRAND_NAME_EXISTED(1023,"Brand does already existed" , HttpStatus.CONFLICT ),
    COMMENT_NOT_FOUND(1024, "Comment does not exist", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(1025, "Cart does not exist", HttpStatus.NOT_FOUND ),
    PRODUCT_IS_OUT_OF_QUANTITY(1026, "Product is out of quantity", HttpStatus.BAD_REQUEST ),
    PRODUCT_NOT_AVAILABLE(1027, "Product is not available at the moment" , HttpStatus.BAD_REQUEST ),
    ADDRESS_NOT_FOUND(1028, "Address does not exist", HttpStatus.NOT_FOUND ),
    PROMOTION_NOT_FOUND(1029,"Promotion does not exist" , HttpStatus.BAD_REQUEST ),
    TOKEN_EXPIRED(1030,"Token is expired" , HttpStatus.UNAUTHORIZED),
    RANK_NOT_FOUND(1031, "Rank level not found", HttpStatus.NOT_FOUND ),
    PROMOTION_NOT_GIFT(1032,"Discount codes do not apply to ranking programs" , HttpStatus.BAD_REQUEST),
    RANK_LEVEL_MIN_SPENDING_INVALID(1033,"minSpending must increase with priority" ,HttpStatus.BAD_REQUEST ),
    OTP_EXPIRED(1034,"Otp expired" , HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(1035,"End date cannot be before start date",HttpStatus.BAD_REQUEST ),
    PROMOTION_IS_ACTIVE(1036,"Promotion is active" , HttpStatus.BAD_REQUEST),
    PROMOTION_IS_EXPIRED(1037, "Promotion is expired", HttpStatus.BAD_REQUEST),
    CAN_NOT_COLLECTED(1038,"This promo code could not be collected" , HttpStatus.BAD_REQUEST),
    PROMOTION_IS_NOT_VALID(1039,"Promotion is not available for this product" ,HttpStatus.BAD_REQUEST ),
    PROMOTION_USED(1040,"This promotion has already been used" ,HttpStatus.BAD_REQUEST ),
    ORDER_VALUE_NOT_ENOUGH(1041,"Order value is not eligible for this promotion", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1042, "Order not found",HttpStatus.NOT_FOUND ),
    INVENTORY_NOT_FOUND(1043, "Inventory not found",HttpStatus.NOT_FOUND ),
    ORDER_CANNOT_BE_CHANGED(1044, "Order cannot be change", HttpStatus.BAD_REQUEST),
    STATUS_INVALID(1045, "Status invalid",HttpStatus.BAD_REQUEST ),
    API_CANNOT_CHANGE_TO_SHIPPING(1046, "Api cannot change to shipping", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_STOCK(1047, "Not enough stock", HttpStatus.BAD_REQUEST),
    CANNOT_PAYMENT(1048,"Cannot payment" , HttpStatus.BAD_REQUEST),
    SUPPLIER_NOT_FOUND(1049, "Supplier not found", HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_NOT_FOUND(1050, "Purchase order not found", HttpStatus.NOT_FOUND),
    PURCHASE_ORDER_CANNOT_BE_UPDATED(1051, "Purchase order cannot be updated",HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_DETAIL_NOT_FOUND(1052, "Purchase order detail not found", HttpStatus.NOT_FOUND),
    QUANTITY_MUST_BE_ONE_FOR_SERIAL_NUMBER(1053, "Quantity must be one for serial number", HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_COMPLETED(1054, "Purchase order completed", HttpStatus.BAD_REQUEST),
    PRODUCT_DETAIL_NOT_MATCH(1055, "Product detail not match", HttpStatus.CONFLICT),
    QUANTITY_EXCEED_PURCHASE_ORDER_DETAIL(1056,"Quantity exceed purchase order detail" ,HttpStatus.CONFLICT),
    SERIAL_NUMBER_QUANTITY_MISMATCH(1057,"Serial number quantity mismatch" ,HttpStatus.BAD_REQUEST ),
    PURCHASE_ORDER_DETAIL_NOT_BELONG_TO_PURCHASE_ORDER(1058,"Purchase order detail not belong to purchase order" , HttpStatus.CONFLICT),
    SERIAL_NUMBER_ALREADY_EXISTS( 1059,"Serial number already exists" ,HttpStatus.CONFLICT),
    QUANTITY_EXCEED_PURCHASE_ORDER(1060, "Quantity exceed purchase order",HttpStatus.CONFLICT),
    GOODS_RECEIPT_NOTE_NOT_FOUND(1061,"Goods receipt note not found",HttpStatus.NOT_FOUND),
    ORDER_INVALID_STATUS(1062, "Order invalid status", HttpStatus.CONFLICT),
    ORDER_DETAIL_NOT_FOUND(1063, "Order detail not found", HttpStatus.NOT_FOUND),
    QUANTITY_EXCEED_ORDER(1064, "Quantity exceed order", HttpStatus.CONFLICT),
    SERIAL_NUMBER_NOT_FOUND(1065, "Serial number not found", HttpStatus.NOT_FOUND),
    SERIAL_NUMBER_SOLD(1066, "Serial number sold", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_MATCH_SERIAL_NUMBER(1067, "Product not match serial number", HttpStatus.CONFLICT),
    SERIAL_NUMBER_NOT_BELONGS_TO_PRODUCT(1068, "Serial number not belong to product", HttpStatus.CONFLICT),
    ADDRESS_NOT_BELONG_TO_USER(1069, "Address not belong to user", HttpStatus.CONFLICT),
    DELIVERY_NOTE_NOT_FOUND(1070, "Delivery note not found", HttpStatus.NOT_FOUND),
    DELIVERY_CAN_NOT_BE_UPDATED(1071, "Delivery can not be updated", HttpStatus.CONFLICT),
    PRODUCT_DETAIL_NOT_BELONG_TO_PRODUCT(1072, "Product detail not belong to product", HttpStatus.CONFLICT),
    INVALID_RATING(1073, "Invalid rating", HttpStatus.CONFLICT),
    PROMOTION_USAGE_LIMIT_EXCEEDED(1074, "The promotion code has reached its maximum usage limit", HttpStatus.CONFLICT),
    RANK_INVALID(1075, "Rank level invalid", HttpStatus.CONFLICT),
    LIST_USER_EMPTY(1076,"User list cannot be empty" , HttpStatus.BAD_REQUEST),
    LIST_PRODUCT_EMPTY(1077, "Product list cannot be empty",HttpStatus.BAD_REQUEST ),
    LIST_RANK_EMPTY(1078, "Rank list cannot be empty", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_COMPLETED(1079, "Order cannot be completed", HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(1080, "Otp not found for user", HttpStatus.NOT_FOUND);

    private final String message;
    private final int code;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
