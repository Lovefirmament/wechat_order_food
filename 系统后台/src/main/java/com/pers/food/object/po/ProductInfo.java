package com.pers.food.object.po;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pers.food.util.DateToLongSerializer;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
