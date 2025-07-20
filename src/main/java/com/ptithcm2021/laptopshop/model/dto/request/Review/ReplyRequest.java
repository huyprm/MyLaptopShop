package com.ptithcm2021.laptopshop.model.dto.request.Review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequest {
    @NotBlank
    private String content;
    @NotBlank
    private String replyToUserid;
    @NotNull
    private long parentId;
    private List<String> reviewImage;
}
