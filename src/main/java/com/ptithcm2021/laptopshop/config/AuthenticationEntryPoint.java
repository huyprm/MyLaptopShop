package com.ptithcm2021.laptopshop.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String e = authException.getMessage();
        ErrorCode errorCode;
        if (e.equals("INVALID_TOKEN")) {
            errorCode = ErrorCode.INVALID_TOKEN;
        } else if (e.equals("TOKEN_EXPIRED")) {
            errorCode = ErrorCode.TOKEN_EXPIRED;
        } else errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper object = new ObjectMapper();

        response.getWriter().write(object.writeValueAsString(apiResponse));
        response.flushBuffer();

    }
}
