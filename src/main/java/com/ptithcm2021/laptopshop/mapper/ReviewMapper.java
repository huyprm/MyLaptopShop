package com.ptithcm2021.laptopshop.mapper;

import com.ptithcm2021.laptopshop.model.dto.request.Review.CommentRequest;
import com.ptithcm2021.laptopshop.model.dto.request.Review.RatingRequest;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ChildReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.ParentReviewResponse;
import com.ptithcm2021.laptopshop.model.dto.response.ReviewResponse.RatingResponse;
import com.ptithcm2021.laptopshop.model.entity.Review;
import com.ptithcm2021.laptopshop.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview (CommentRequest commentRequest);
    Review toReview (RatingRequest ratingRequest);

    @Mapping(target = "username", source = "reviewer", qualifiedByName = "username")
    @Mapping(target = "userId", source = "reviewer.id")
    @Mapping(target = "productDetailId", source = "productDetail.id")
    @Mapping(target = "childReviewResponses", ignore = true)
    ParentReviewResponse toResponse(Review review);

    @Mapping(target = "username", source = "reviewer", qualifiedByName = "username")
    @Mapping(target = "replyOnUser", source = "replyOnUser", qualifiedByName = "username")
    @Mapping(target = "parentId", source = "parentReview.id")
    @Mapping(target = "userId", source = "reviewer.id")
    ChildReviewResponse toChildResponse(Review review);

    @Named("username")
    default String getFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Mapping(target = "username", source = "reviewer", qualifiedByName = "username")
    RatingResponse toRatingResponse(Review review);
}
