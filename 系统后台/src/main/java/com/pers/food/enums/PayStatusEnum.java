package com.pers.food.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum {
    WAIT(0,"等待支付"),
    FINISHED(1,"支付成功"),
    CANCEL(2,"取消支付");
    private Integer code;
    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
