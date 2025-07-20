package com.ptithcm2021.laptopshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.ptithcm2021.laptopshop.model.dto.request.GoogleAuthRequest;
import com.ptithcm2021.laptopshop.model.dto.request.LoginRequest;
import com.ptithcm2021.laptopshop.model.dto.request.ResetPasswordRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.GoogleTokenResponse;
import com.ptithcm2021.laptopshop.model.enums.LoginTypeEnum;
import com.ptithcm2021.laptopshop.service.AuthenticationService;
import com.ptithcm2021.laptopshop.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse httpServletResponse) throws ParseException, JOSEException, JOSEException {
        return ApiResponse.<String>builder()
                .data(authenticationService.login(loginRequest, httpServletResponse))
                .build();
    }

    @GetMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ApiResponse.<String>builder()
                .message("Successfully logged out")
                .build();
    }

    @GetMapping("/refresh-token")
    public ApiResponse<String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws KeyLengthException {
        return ApiResponse.<String>builder().data(authenticationService.refreshToken(request, response)).build();
    }

    @GetMapping("/verify-otp")
    public ApiResponse<String> verifyOTP(@RequestParam String otp, @RequestParam String email) throws KeyLengthException {
        return ApiResponse.<String>builder().data(authenticationService.verifyOTP(email, otp)).build();
    }

    @GetMapping("/send-otp")
    public ApiResponse<String> sendOtp(@RequestParam String mail) throws MessagingException {
        authenticationService.sendOTPViaMail(mail);
        return ApiResponse.<String>builder().message("Send OTP successful").build();
    }

    @GetMapping("/send-otp-forgot-pw")
    public ApiResponse<String> sendOtpForgotPw(@RequestParam String mail) throws MessagingException {
        authenticationService.sendOTPForgotPw(mail);
        return ApiResponse.<String>builder().message("Send OTP successful").build();
    }

    @PostMapping("/google")
    public ApiResponse<String> authenticateWithGoogle(@RequestBody @Valid GoogleAuthRequest request,
                                                      HttpServletResponse servletResponse) throws JsonProcessingException, KeyLengthException {
        //  Đổi code lấy token
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", request.getCode());
        params.add("redirect_uri", request.getRedirectUri());
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                entity,
                GoogleTokenResponse.class
        );

        GoogleTokenResponse tokenResponse = response.getBody();

        if (tokenResponse == null || tokenResponse.getIdToken() == null) {
            return ApiResponse.<String>builder().code(400).message("Invalid Google Token").build();
        }

        //  Giải mã id_token lấy email
        String[] parts = tokenResponse.getIdToken().split("\\.");
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode payload = mapper.readTree(payloadJson);

        String email = payload.get("email").asText();
        String givenName = payload.has("given_name") ? payload.get("given_name").asText() : null;
        String familyName = payload.has("family_name") ? payload.get("family_name").asText() : null;

        return ApiResponse.<String>builder()
                .data(authenticationService
                        .loginWithIdentifier(email, LoginTypeEnum.GOOGLE, familyName, givenName, servletResponse )).build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authenticationService.resetPassword(request);
        return ApiResponse.<Void>builder().message("Reset password successful").build();
    }
}
