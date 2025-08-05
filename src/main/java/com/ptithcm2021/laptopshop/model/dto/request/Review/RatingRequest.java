package com.ptithcm2021.laptopshop.model.dto.request.Review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @NotBlank
    private String content;
    private List<String> reviewImage;
    @NotNull
    private Long productDetailId;
    @NotNull
    private Long orderId;
    @NotNull
    private Integer rating; // Rating value from 1 to 5
}
