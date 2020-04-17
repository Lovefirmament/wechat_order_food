package com.pers.food.controller;

import com.github.pagehelper.PageInfo;
import com.pers.food.exception.OrderException;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.form.LoginForm;
import com.pers.food.object.po.SellerInfo;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.object.vo.UserVO;
import com.pers.food.service.LoginService;
import com.pers.food.service.OrderService;
import com.pers.food.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/seller/order")
@CrossOrigin
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/orderinfolist")
    public ResultVO orderinfolist(@RequestParam(value = "pageNum",defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize",defaultValue = "5") Integer size
                                ,String query,String property){
        PageInfo orderDTOPageInfo=orderService.getOrderInfoList(property,query, page, size);
        return ResultUtils.success(orderDTOPageInfo);
    }
    @GetMapping("/orderdetaillist/{orderId}")
    public ResultVO getOrderDetailList(@PathVariable String orderId){
        OrderDTO orderDTO=orderService.getOrderOne(orderId);
        return ResultUtils.success(orderDTO);
    }
    @PutMapping("/finish/{orderId}")
    public ResultVO finishOrder(@PathVariable String orderId){
        try {
            OrderDTO orderDTO = orderService.getOrderOne(orderId);
            orderService.finish(orderDTO);
        }catch (OrderException e){
            log.error("完结订单异常{}",e);
            return ResultUtils.fail("完结订单异常");
        }
        return ResultUtils.success();
    }
}
