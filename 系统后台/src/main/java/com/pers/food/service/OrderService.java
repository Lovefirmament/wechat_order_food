package com.pers.food.service;


import com.github.pagehelper.PageInfo;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.po.OrderMaster;

import java.util.List;

public interface OrderService {
    //创建订单
     OrderDTO createOrder(OrderDTO orderDto);
    /** 查询单个订单. */
    OrderDTO getOrderOne(String orderId);
    /** 查询订单列表. */
    PageInfo<OrderDTO> getOrderInfoList(String buyerOpenid, Integer page,Integer size);
    /** 取消订单. */
    OrderDTO cancel(OrderDTO orderDTO);
    /** 完结订单. */
    OrderDTO finish(OrderDTO orderDTO);
    /** 支付订单. */
    OrderDTO paid(OrderDTO orderDTO);
    /**查询所有订单列表**/
    PageInfo<OrderDTO> getOrderInfoList(String property,String query,Integer page,Integer size);

}
