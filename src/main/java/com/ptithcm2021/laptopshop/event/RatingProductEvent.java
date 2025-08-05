package com.ptithcm2021.laptopshop.event;

import com.ptithcm2021.laptopshop.model.entity.Review;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RatingProductEvent extends ApplicationEvent {

    private final Long productDetailId;
    private final Integer oldRating; // null nếu thêm mới
    private final Integer newRating; // null nếu xoá
    private final String type;
    public RatingProductEvent(Long reviewId, Long productDetailId, Integer oldRating, Integer newRating, String type) {
        super(reviewId);
        this.productDetailId = productDetailId;
        this.newRating = newRating;
        this.oldRating = oldRating;
        this.type = type;
    }

    public Review getReview() {
        return (Review) getSource();
    }
}
