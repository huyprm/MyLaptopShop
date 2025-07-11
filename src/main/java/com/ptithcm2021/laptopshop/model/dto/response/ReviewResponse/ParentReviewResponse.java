package com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentReviewResponse {
    private long id;
    private String content;
    private String username;
    private long productDetailId;
    private LocalDateTime reviewDate;
    private List<String> reviewImages;
    private List<Long> childReviewResponses;
}
