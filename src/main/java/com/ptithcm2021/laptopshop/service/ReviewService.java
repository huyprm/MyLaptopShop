package com.ptithcm2021.laptopshop.service;

import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.RatingRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.ReplyRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.RatingResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ReviewService {
    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    ParentReviewResponse addComment(CommentRequest commentRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    ChildReviewResponse addReply(ReplyRequest replyRequest);

    List<ParentReviewResponse> getParentReviews(long productDetailId);

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    void deleteReview(long reviewId);

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    RatingResponse addRating(RatingRequest ratingRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_USER', 'SCOPE_OWNER')")
    RatingResponse updateRating(RatingRequest ratingRequest, long reviewId);

    PageWrapper<RatingResponse> getRatingsByProductDetailId(long productDetailId, int page, int size);
}
