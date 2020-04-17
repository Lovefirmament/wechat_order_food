package com.pers.food.object.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.util.DateToLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductSellerVO implements Serializable {

    private static final long serialVersionUID = 6327590721185994128L;
    private String categoryName;
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    private Integer productStatus;
    private Integer categoryType;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date createTime;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date updateTime;
}
