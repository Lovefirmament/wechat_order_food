package com.pers.food.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    PRODUCT_NOT_EXIST(1,"商品不存在"),
    PRODUCT_OUT_OF_STOCK(2,"库存不足"),
    PRODUCT_UPDATE_ERROR(3,"商品更新失败"),
    PRODUCT_INSERT_ERROR(3,"商品新增失败"),
    PRODUCT_DELETE_ERROR(3,"商品删除失败"),
    PRODUCT_STATE_ERROR(4,"商品状态错误"),
    PARAM_ERROR(11,"参数错误"),
    CART_EMPTY(12,"购物车为空"),
    ORDER_NOT_EXIST(21,"订单不存在"),
    ORDERDETAIL_NOT_EXIST(22,"订单详情不存在"),
    ORDER_STATE_ERROR(23,"订单状态错误"),
    ORDER_DETAIL_EMPTY(23,"订单详情为空"),
    ORDER_UPDATE_FAIL(24,"订单更新失败"),
    ORDER_INSERT_FAIL(25,"订单插入失败"),
    ORDER_PAY_STATUS_ERROR(26,"订单支付状态错误"),
    ORDER_OWNER_ERROR(27,"订单OWNER错误"),
    WECHAT_MP_ERROR(31,"微信公众号错误"),
    LOGIN_STATE_ERROR(41,"登录状态异常"),
    TOKEN_NOT_FOUND(42,"token不存在");

    private int code;
    private String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
