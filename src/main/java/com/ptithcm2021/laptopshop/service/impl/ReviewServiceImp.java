package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.ReplyRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.Review;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.repository.ReviewRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.ReviewService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ReviewRepository reviewRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;
    @Override
    public ParentReviewResponse addComment(CommentRequest commentRequest) {
        String userId = FetchUserIdUtil.fetchUserId();
        ProductDetail productDetail = productDetailRepository
                .findById(commentRequest.getProductDetailId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Review review = Review.builder()
                .productDetail(productDetail)
                .reviewer(user)
                .content(commentRequest.getContent())
                .reviewImages(commentRequest.getReviewImage())
                .build();

        reviewRepository.save(review);

        ParentReviewResponse parentReviewResponse = ParentReviewResponse.builder()
                .id(review.getId())
                .reviewDate(review.getReviewDate())
                .content(review.getContent())
                .reviewImages(review.getReviewImages())
                .userId(userId)
                .username(review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName())
                .build();

        simpMessagingTemplate.convertAndSend("/topic/comments/" + productDetail.getId(), parentReviewResponse);
        return parentReviewResponse;
    }

    @Override
    public ChildReviewResponse addReply(ReplyRequest replyRequest) {
        String userId = FetchUserIdUtil.fetchUserId();

        Review parent = reviewRepository.findById(replyRequest.getParentId())
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        User replier =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User tag = userRepository.findById(replyRequest.getReplyToUserid())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Review reply = Review.builder()
                .reviewImages(replyRequest.getReviewImage())
                .content(replyRequest.getContent())
                .replyOnUser(tag)
                .reviewer(replier)
                .content(replyRequest.getContent())
                .parentReview(parent)
                .productDetail(parent.getProductDetail())
                .build();

        reviewRepository.save(reply);

        ChildReviewResponse childReviewResponse = ChildReviewResponse.builder()
                .id(reply.getId())
                .reviewDate(reply.getReviewDate())
                .content(reply.getContent())
                .reviewImages(reply.getReviewImages())
                .userId(reply.getReviewer().getId())
                .username(reply.getReviewer().getFirstName() + " " + reply.getReviewer().getLastName())
                .parentId(parent.getId())
                .replyOnUser(reply.getReplyOnUser().getFirstName() + " " + reply.getReplyOnUser().getLastName())
                .build();

        simpMessagingTemplate.convertAndSend("/topic/comments/" + parent.getProductDetail().getId(), childReviewResponse);
        return childReviewResponse;
    }

    @Override
    public List<ParentReviewResponse> getParentReviews(long productDetailId) {
        List<Review> reviews = reviewRepository.findReviewParentByProductDetailId(productDetailId);

        List<ParentReviewResponse> responses = new ArrayList<>();

        reviews.forEach(review -> {
            ParentReviewResponse parentReviewResponse = ParentReviewResponse.builder()
                    .id(review.getId())
                    .productDetailId(productDetailId)
                    .reviewDate(review.getReviewDate())
                    .content(review.getContent())
                    .reviewImages(review.getReviewImages())
                    .userId(review.getReviewer().getId())
                    .username(review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName())
                    .childReviewResponses(reviewRepository.findAllByParentReviewId(review.getId()).stream().map(reply -> ChildReviewResponse.builder()
                            .id(reply.getId())
                            .reviewDate(reply.getReviewDate())
                            .content(reply.getContent())
                            .reviewImages(reply.getReviewImages())
                            .userId(reply.getReviewer().getId())
                            .username(reply.getReviewer().getFirstName() + " " + reply.getReviewer().getLastName())
                            .parentId(review.getId())
                            .replyOnUser(reply.getReplyOnUser().getFirstName() + " " + reply.getReplyOnUser().getLastName())
                            .build()).toList())
                    .build();
            responses.add(parentReviewResponse);
        });
        return responses;
    }

    @Override
    public ChildReviewResponse getChildReviewById(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        return ChildReviewResponse.builder()
                .id(review.getId())
                .reviewDate(review.getReviewDate())
                .content(review.getContent())
                .reviewImages(review.getReviewImages())
                .userId(review.getReviewer().getId())
                .username(review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName())
                //.childReviewResponses(reviewRepository.findAllByParentReviewId(review.getId()))
                .replyOnUser(review.getReplyOnUser().getFirstName() + " " + review.getReplyOnUser().getLastName())
                .build();
    }

    @Override
    public void deleteReview(long reviewId) {
        String userId = FetchUserIdUtil.fetchUserId();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!review.getReviewer().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }
}
