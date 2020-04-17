package com.pers.food.object.po;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderMaster implements Serializable {
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer payStatus;
    private Date createTime;
    private Date updateTime;
}
