package com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private long id;
    private String content;
    private int rating;
    private String username;
    private String avatarUrl;
    private LocalDateTime reviewDate;
    private List<String> reviewImages;
}
