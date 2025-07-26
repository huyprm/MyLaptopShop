package com.ptithcm2021.laptopshop.exception;

import lombok.Getter;

import javax.validation.constraints.Null;

@Getter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
    }

}
