package com.pers.food.object.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pers.food.object.po.OrderDetail;
import com.pers.food.util.DateToLongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer payStatus;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date createTime;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date updateTime;
    private List<OrderDetail> orderDetailList;

}
