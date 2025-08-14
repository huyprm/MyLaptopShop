package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.event.EventPublisherHelper;
import com.ptithcm2021.laptopshop.event.RatingProductEvent;
import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.ReviewMapper;
import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.RatingRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.ReplyRequest;
import com.ptithcm2021.laptopshop.model.dto.response.PageWrapper;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.RatingResponse;
import com.ptithcm2021.laptopshop.model.entity.Order;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.Review;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.model.enums.OrderStatusEnum;
import com.ptithcm2021.laptopshop.repository.OrderRepository;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.repository.ReviewRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.ReviewService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ReviewRepository reviewRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final OrderRepository orderRepository;
    private final EventPublisherHelper eventPublisherHelper;

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

        ParentReviewResponse parentReviewResponse = reviewMapper.toResponse(review);

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

        ChildReviewResponse childReviewResponse = reviewMapper.toChildResponse(reply);

        simpMessagingTemplate.convertAndSend("/topic/comments/" + parent.getProductDetail().getId(), childReviewResponse);
        return childReviewResponse;
    }

    @Override
    public List<ParentReviewResponse> getParentReviews(long productDetailId) {
        List<Review> reviews = reviewRepository.findReviewParentByProductDetailId(productDetailId);

        return reviews.stream().map(review -> {
            List<ChildReviewResponse> childResponses = reviewRepository.findAllByParentReviewId(review.getId())
                    .stream()
                    .map(reviewMapper::toChildResponse)
                    .toList();

            ParentReviewResponse response = reviewMapper.toResponse(review);

            response.setChildReviewResponses(childResponses);
            return response;
        }).toList();
    }

    @Override
    public void deleteReview(long reviewId) {
        String userId = FetchUserIdUtil.fetchUserId();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!review.getReviewer().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if( review.getOrder() != null){
            eventPublisherHelper.publish(new RatingProductEvent(
                review.getId(),
                review.getProductDetail().getId(),
                review.getRating(),
                null,
                "delete"
            ));
        }

        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public RatingResponse addRating(RatingRequest ratingRequest) {
        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) {
            throw new AppException(ErrorCode.INVALID_RATING);
        }

        String userId = FetchUserIdUtil.fetchUserId();

        ProductDetail productDetail = productDetailRepository
                .findById(ratingRequest.getProductDetailId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userRepository.getReferenceById(userId);

        Order order = orderRepository.findById(ratingRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus()!= OrderStatusEnum.COMPLETED){
            throw new AppException(ErrorCode.CANNOT_RATE_BEFORE_COMPLETED);
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_MATCH_USER);
        }
        Review review = reviewMapper.toReview(ratingRequest);
        review.setProductDetail(productDetail);
        review.setReviewer(user);
        review.setOrder(order);

        reviewRepository.save(review);

        eventPublisherHelper.publish(new RatingProductEvent(
                review.getId(),
                productDetail.getId(),
                null,
                ratingRequest.getRating(),
                "create"));

        return reviewMapper.toRatingResponse(review);
    }

    @Override
    public RatingResponse updateRating(RatingRequest ratingRequest, long reviewId) {
        if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) {
            throw new AppException(ErrorCode.INVALID_RATING);
        }

        String userId = FetchUserIdUtil.fetchUserId();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!review.getReviewer().getId().equals(userId)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_MATCH_USER);
        }

        int oldRating = review.getRating();

        review.setRating(ratingRequest.getRating());
        review.setContent(ratingRequest.getContent());
        review.setReviewImages(ratingRequest.getReviewImage());

        reviewRepository.save(review);

        eventPublisherHelper.publish(new RatingProductEvent(
                review.getId(),
                review.getProductDetail().getId(),
                oldRating,
                ratingRequest.getRating(),
                "update"));

        return reviewMapper.toRatingResponse(review);
    }

    @Override
    public PageWrapper<RatingResponse> getRatingsByProductDetailId(long productDetailId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reviewDate"));
        Page<Review> reviews = reviewRepository.findAllRatingByProductDetailId(productDetailId, pageable);
        return PageWrapper.<RatingResponse>builder()
                .content(reviews.getContent().stream()
                        .map(reviewMapper::toRatingResponse)
                        .toList())
                .pageNumber(reviews.getNumber())
                .pageSize(reviews.getSize())
                .totalElements(reviews.getTotalElements())
                .totalPages(reviews.getTotalPages())
                .build();
    }

    @Override
    public RatingResponse getRatingByOrderId(long orderId) {
        String userId = FetchUserIdUtil.fetchUserId();
        Review review = reviewRepository.findByOrderId((orderId))
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));
        log.info(review.getReviewer().getId());
        if(!review.getReviewer().getId().equals(userId)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_MATCH_USER);
        }
        return reviewMapper.toRatingResponse(review);
    }
}
