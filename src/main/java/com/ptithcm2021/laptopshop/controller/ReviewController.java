package com.ptithcm2021.laptopshop.controller;

import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.RatingRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.ReplyRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ApiResponse;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.RatingResponse;
import com.ptithcm2021.laptopshop.model.entity.Review;
import com.ptithcm2021.laptopshop.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    //@MessageMapping("/comment")
    //@SendTo("/topic/comments")
    @PostMapping("/comment")
    public ApiResponse<ParentReviewResponse> comment(@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<ParentReviewResponse>builder().data(reviewService.addComment(request)).build();
    }

    @PostMapping("/reply")
    public ApiResponse<ChildReviewResponse> reply(@RequestBody @Valid ReplyRequest request) {
        return ApiResponse.<ChildReviewResponse>builder().data(reviewService.addReply(request)).build();
    }

    @GetMapping("/comments")
    public ApiResponse<List<ParentReviewResponse>> getAllComments(@RequestParam long productDetailId) {
        return ApiResponse.<List<ParentReviewResponse>>builder()
                .data(reviewService.getParentReviews(productDetailId)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ApiResponse.<Void>builder()
                .message("Review deleted successfully")
                .build();
    }

    @PostMapping("/rating")
    public ApiResponse<RatingResponse> addRating(@RequestBody @Valid RatingRequest request){
        RatingResponse ratingResponse = reviewService.addRating(request);
        return ApiResponse.<RatingResponse>builder()
                .data(ratingResponse)
                .message("Rating added successfully")
                .build();
    }

    @PutMapping("/rating/{reviewId}")
    public ApiResponse<RatingResponse> updateRating(@RequestBody @Valid RatingRequest request,
                                                    @PathVariable Long reviewId) {
        RatingResponse ratingResponse = reviewService.updateRating(request, reviewId);
        return ApiResponse.<RatingResponse>builder()
                .data(ratingResponse)
                .message("Rating updated successfully")
                .build();
    }

    @GetMapping("/ratings")
    public ApiResponse<PageWrapper<RatingResponse>> getRatingsByProductDetailId(
            @RequestParam long productDetailId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<PageWrapper<RatingResponse>>builder()
                .data(reviewService.getRatingsByProductDetailId(productDetailId, page, size))
                .message("Ratings retrieved successfully")
                .build();
    }
}
