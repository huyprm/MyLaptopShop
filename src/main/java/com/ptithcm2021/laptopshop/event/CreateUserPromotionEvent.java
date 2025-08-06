package com.ptithcm2021.laptopshop.event;

import com.ptithcm2021.laptopshop.model.entity.Promotion;
import com.ptithcm2021.laptopshop.model.entity.UserPromotion;
import lombok.Generated;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Set;

@Getter
public class CreateUserPromotionEvent extends ApplicationEvent {
    private final Set<String> userIds;
    public CreateUserPromotionEvent(Set<String> userIds, Promotion promotion) {
        super(promotion);
        this.userIds = userIds;
    }

    public Promotion getPromotion() {
        return (Promotion) getSource();
    }
}
