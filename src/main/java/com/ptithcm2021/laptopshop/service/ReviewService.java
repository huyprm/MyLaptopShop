package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.ReplyRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ReviewService {

    ParentReviewResponse addComment(CommentRequest commentRequest);

    ChildReviewResponse addReply(ReplyRequest replyRequest);

    List<ParentReviewResponse> getParentReviews(long productDetailId);

    ChildReviewResponse getChildReviewById(long id);
}
