package com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildReviewResponse {
    private long id;
    private long parentId;
    private String userId;
    private String avatarUrl;
    private String username;
    private String replyOnUser;
    private LocalDateTime reviewDate;
    private List<String> reviewImages;
    private String content;
}
