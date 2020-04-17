package com.pers.food.service;


import com.pers.food.object.dto.OrderDTO;

public interface BuyerService {

    //查询一个订单
    OrderDTO getOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
