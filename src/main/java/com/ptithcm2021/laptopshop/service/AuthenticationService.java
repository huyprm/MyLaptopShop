package com.ptithcm2021.laptopshop.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.ptithcm2021.laptopshop.model.dto.request.LoginRequest;
import com.ptithcm2021.laptopshop.model.dto.request.ResetPasswordRequest;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.text.ParseException;

public interface AuthenticationService {
    String refreshToken(HttpServletRequest request, HttpServletResponse response) throws KeyLengthException;
    boolean verifyAccessToken(String token) throws ParseException, JOSEException;
    String login(LoginRequest request, HttpServletResponse response) throws ParseException, JOSEException;
    void logout(HttpServletRequest request, HttpServletResponse response);
    String verifyOTP(String email, String otp);
    void sendOTPViaMail (String email) throws MessagingException;
    String loginWithIdentifier(String mail, LoginTypeEnum loginType, String firstName, String lastName, HttpServletResponse response) throws KeyLengthException;
    void resetPassword(ResetPasswordRequest request);
    void sendOTPForgotPw(String email) throws MessagingException;
}
