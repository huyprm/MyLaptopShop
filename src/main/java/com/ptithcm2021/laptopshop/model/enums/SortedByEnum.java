package com.ptithcm2021.laptopshop.model.enums;

import lombok.Getter;

@Getter
public enum SortedByEnum {
    RATING ("Evaluate"),
    NEW( "New goods"),
    PRICE ("Price"),
    SOLD ("Selling well")
    ;
    private final String label;

    SortedByEnum(String label) {
        this.label = label;
    }

}
