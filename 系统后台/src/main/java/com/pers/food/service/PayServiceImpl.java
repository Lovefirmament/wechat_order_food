package com.pers.food.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class PayServiceImpl implements  PayService {
    @Autowired
    private OrderService orderService;
    private static final String ORDER_NAME="微信订单";

    @Autowired
    private BestPayServiceImpl bestPayService;
    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest=new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信支付】payRequest={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("【微信支付】payResponse={}",JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify( String notifyData)
    {
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);
        log.info("【微信异步通知】,payResonse={}",JsonUtil.toJson(payResponse));
        OrderDTO orderDTO=orderService.getOrderOne(payResponse.getOrderId());
        orderService.paid(orderDTO);
        return payResponse;
    }

    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest=new RefundRequest();
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信退款】request={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse=bestPayService.refund(refundRequest);
        log.info("【微信退款】response={}",JsonUtil.toJson(refundRequest));
        return refundResponse;
    }
}
