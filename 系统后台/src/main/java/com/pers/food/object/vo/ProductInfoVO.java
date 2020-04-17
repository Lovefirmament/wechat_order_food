package com.pers.food.object.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ProductInfoVO implements Serializable {

    private static final long serialVersionUID = -2389947852046347781L;
    @JsonProperty("id")
    private String productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("price")
    private BigDecimal productPrice;
    @JsonProperty("description")
    private String productDescription;
    @JsonProperty("icon")
    private String productIcon;
}
