package com.ptithcm2021.laptopshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    UNAUTHORIZED (1000, "Unauthorized", HttpStatus.UNAUTHORIZED),
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
    CANNOT_DELETE(1012,"Cannot delete role" , HttpStatus.CONFLICT ),
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
    ;

    private final String message;
    private final int code;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
