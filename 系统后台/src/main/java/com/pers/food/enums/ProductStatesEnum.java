package com.pers.food.enums;

import lombok.Getter;

@Getter
public enum ProductStatesEnum {
    UP(0,"在架"),
    down(1,"下架"),
    ;
    private Integer code;
    private String message;

    ProductStatesEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
