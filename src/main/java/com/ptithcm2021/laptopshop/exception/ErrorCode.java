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
