package com.pers.food.object.converter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.form.OrderForm;
import com.pers.food.object.po.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderFormToOrderDTO {

    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDto=new OrderDTO();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList=new ArrayList<>();
        Gson gson=new Gson();
        try {
             orderDetailList=gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        }catch (Exception e){
            log.error("json转换错误，string={}",orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);

        return orderDto;
    }
}
