package com.pers.food.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pers.food.enums.OrderStatusEnum;
import com.pers.food.enums.PayStatusEnum;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import com.pers.food.mapper.OrderDetailMapper;
import com.pers.food.mapper.OrderMasterMapper;
import com.pers.food.object.converter.OrderMasterToOrderDTO;
import com.pers.food.object.dto.CartDTO;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.po.OrderDetail;
import com.pers.food.object.po.OrderMaster;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.util.KeyUtil;
import com.pers.food.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private PayService payService;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO){
        String orderId= KeyUtil.genUniqueKey();
        //查询商品信息
        List<String> productIdList=orderDTO.getOrderDetailList().stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfo>productInfoList=productService.getProductInfoInId(productIdList);
        //计算总价
        BigDecimal orderAmount=new BigDecimal(0);
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            for(ProductInfo productInfo:productInfoList){
                if(productInfo.getProductId().equals(orderDetail.getProductId())){
                    orderAmount=productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    orderDetail.setProductName(productInfo.getProductName());
                    orderDetail.setProductPrice(productInfo.getProductPrice());
                    orderDetail.setProductIcon(productInfo.getProductIcon());
                }
            }
        }
        //扣库存
        List<CartDTO> cartDTOList=orderDTO.getOrderDetailList().stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);
        //订单入库
        OrderMaster orderMaster=new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        int insertResult=orderMasterMapper.insertSelective(orderMaster);
        if(insertResult==0){
            throw new OrderException(ResultEnum.ORDER_INSERT_FAIL);
        }
        //订单详情入库
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList())
        {
            insertResult=orderDetailMapper.insertSelective(orderDetail);
            if(insertResult==0){
                throw new OrderException(ResultEnum.ORDER_INSERT_FAIL);
            }
        }

        return getOrderOne(orderId);
    }

    @Override
    public OrderDTO getOrderOne(String orderId) {
        //查询order
        OrderMaster orderMasterQuery=new OrderMaster();
        orderMasterQuery.setOrderId(orderId);
        OrderMaster orderMaster=orderMasterMapper.selectOne(orderMasterQuery);
        if(orderMaster==null){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //查orderDetail
        OrderDetail orderDetailQuery=new OrderDetail();
        orderDetailQuery.setOrderId(orderId);
        List<OrderDetail> orderDetailList=orderDetailMapper.select(orderDetailQuery);
        if(CollectionUtils.isEmpty(orderDetailList))
        {
            throw new OrderException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        //组装数据
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public PageInfo<OrderDTO> getOrderInfoList(String buyerOpenid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setBuyerOpenid(buyerOpenid);
        List<OrderMaster> orderMasterList=orderMasterMapper.select(orderMaster);
        List<OrderDTO> orderDTOList= OrderMasterToOrderDTO.convert(orderMasterList);
        PageInfo<OrderDTO> pageInfo=new PageInfo<>(orderDTOList);
        return pageInfo;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster=new OrderMaster();
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
        {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATE_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster,"updateTime");
       int i=orderMasterMapper.updateByPrimaryKeySelective(orderMaster);
        if(i==0)
        {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new OrderException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        if(orderDTO.getPayStatus().equals(PayStatusEnum.FINISHED.getCode()))
        {
            payService.refund(orderDTO);
        }
        //增加回库存
        List<CartDTO> cartDTOList=orderDTO.getOrderDetailList().stream()
                .map(e->new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        return getOrderOne(orderDTO.getOrderId());
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
        {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATE_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster,"updateTime");
        int updateResult=orderMasterMapper.updateByPrimaryKeySelective(orderMaster);
        if(updateResult==0)
        {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return getOrderOne(orderDTO.getOrderId());
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {

        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
        {
            log.error("【订单支付】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATE_ERROR);
        }

        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new OrderException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        orderDTO.setPayStatus(PayStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster,"updateTime");
        int updateResult=orderMasterMapper.updateByPrimaryKeySelective(orderMaster);
        if(updateResult==0)
        {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return getOrderOne(orderDTO.getOrderId());
    }


    @Override
    public PageInfo<OrderDTO> getOrderInfoList(String property,String query, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrderMaster> orderMasterList=null;
        if(!StringUtils.isEmpty(property)&&!StringUtils.isEmpty(query)) {
            Example example=new Example(OrderMaster.class);
            String condition = "%" + query + "%";
            example.createCriteria().andLike(property, condition);
            orderMasterList=orderMasterMapper.selectByExample(example);
        }else{
            Example example=new Example(OrderMaster.class);
            example.orderBy("createTime").desc();
            orderMasterList=orderMasterMapper.selectByExample(example);
        }
        List<OrderDTO> orderDTOList= OrderMasterToOrderDTO.convert(orderMasterList);
        PageInfo<OrderMaster> pageInfo=new PageInfo<>(orderMasterList);
        //将PageInfo中的orderMasterList换成orderDTOList
        PageInfo<OrderDTO> pageInfoDTO=PageUtils.PageInfoToPageInfoVo(pageInfo,orderDTOList);
        return pageInfoDTO;
    }
}
