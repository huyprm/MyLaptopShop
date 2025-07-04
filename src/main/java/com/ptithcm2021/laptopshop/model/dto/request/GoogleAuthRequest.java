package com.ptithcm2021.laptopshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthRequest {
    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "RedirectUri cannot be blank")
    private String redirectUri;
}
