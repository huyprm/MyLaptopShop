package com.ptithcm2021.laptopshop.listener;

import com.ptithcm2021.laptopshop.event.RatingProductEvent;
import com.ptithcm2021.laptopshop.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRatingListener {
    private final ProductDetailService productDetailService;
    @Async
    @EventListener
    public void handle(RatingProductEvent event) {
        Long productId = event.getProductDetailId();
        Integer oldRating = event.getOldRating();
        Integer newRating = event.getNewRating();

        switch (event.getType()) {
            case "create" -> productDetailService.handleAddRating(productId, newRating);
            case "update" -> productDetailService.handleUpdateRating(productId, oldRating, newRating);
            case "delete" -> productDetailService.handleDeleteRating(productId, oldRating);
        }

    }
}
