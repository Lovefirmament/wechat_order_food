package com.pers.food.controller;

import com.lly835.bestpay.model.PayResponse;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.service.OrderService;
import com.pers.food.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("returnUrl")String returnUrl,
                               Map<String,Object> map) {
        //查询订单
        OrderDTO orderDTO=orderService.getOrderOne(orderId);
        if(orderDTO==null){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //配置创建支付所需的必要信息
        PayResponse payResponse=payService.create(orderDTO);
        //把信息传入支付模板中
        map.put("payResponse",payResponse);
        returnUrl="http://foodshops.natapp1.cc/#/order/"+orderId;
        map.put("returnUrl",returnUrl);
        return new ModelAndView("pay/create",map);
    }
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData)
    {
        payService.notify(notifyData);
        return new ModelAndView("pay/success");
    }
}
