package com.pers.food.object.po;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pers.food.util.DateToLongSerializer;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetail implements Serializable {
    @Id
    private String detailId;
    private String orderId;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private String productIcon;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date createTime;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date updateTime;
}
