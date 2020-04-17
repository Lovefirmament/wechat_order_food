package com.pers.food.service;

import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.pers.food.object.dto.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PayServiceImplTest {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;
    @Test
    void create() {
        OrderDTO orderDTO=orderService.getOrderOne("1582391421948337591");

        payService.create(orderDTO);
    }
}