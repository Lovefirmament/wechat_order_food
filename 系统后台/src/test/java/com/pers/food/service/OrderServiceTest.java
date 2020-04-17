package com.pers.food.service;

import com.github.pagehelper.PageInfo;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Test
    void createOrder() {
    }

    @Test
    void findOne() {
        OrderDTO orderDTO=orderService.getOrderOne("1582195822602835340");
        log.info("查询单个订单{}",orderDTO);
        Assertions.assertEquals("1582195822602835340",orderDTO.getOrderId());
    }

    @Test
    void findList() {
    }

    @Test
    void cancel() {
        OrderDTO orderDTO=orderService.getOrderOne("1582215039882870352");
        OrderDTO orderDTO1=orderService.cancel(orderDTO);
        log.info("查询单个订单{}",orderDTO1);
    }

    @Test
    void finish() {
        OrderDTO orderDTO=orderService.getOrderOne("1582214807822873716");
        OrderDTO orderDTO1=orderService.finish(orderDTO);
        log.info("查询单个订单{}",orderDTO1);
    }

    @Test
    void paid() {
        OrderDTO orderDTO=orderService.getOrderOne("1582730673532412747");
        OrderDTO orderDTO1=orderService.paid(orderDTO);
        log.info("查询单个订单{}",orderDTO1);
    }

}