package com.pers.food.controller;


import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import com.pers.food.object.converter.OrderFormToOrderDTO;
import com.pers.food.object.dto.CartDTO;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.dto.People;
import com.pers.food.object.form.OrderForm;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.service.BuyerService;
import com.pers.food.service.OrderService;
import com.pers.food.util.JsonUtil;
import com.pers.food.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerService buyerService;
    /*
    1、参数校验
    2、查询商品信息
    3、计算总价
    4、扣库存
    5、订单入库
     */

    @PostMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.error("创建订单，参数不正确，orderForm={}",orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        //orderForm->orderDTO
        OrderDTO orderDto= OrderFormToOrderDTO.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDto.getOrderDetailList()))
        {
            log.error("创建订单，购物车为空");
            throw new OrderException(ResultEnum.CART_EMPTY);
        }
        //创建订单
        OrderDTO result=orderService.createOrder(orderDto);
        Map<String,String> map=new HashMap<>();
        map.put("orderId",orderDto.getOrderId());
        return ResultUtils.success(map);
    }

    @GetMapping("/list")
    public ResultVO list(@RequestParam("openid") String openid,
                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                         @RequestParam(value = "size", defaultValue = "2") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】openid为空");
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        List<OrderDTO> orderDTOList=orderService.getOrderInfoList(openid,page,size).getList();
        System.out.println(orderDTOList);
        return ResultUtils.success(orderDTOList);
    }

    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId) {
        OrderDTO orderDTO = buyerService.getOrderOne(openid,orderId);
        return ResultUtils.success(orderDTO);
    }
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        buyerService.cancelOrder(openid, orderId);
        return ResultUtils.success();
    }
}
