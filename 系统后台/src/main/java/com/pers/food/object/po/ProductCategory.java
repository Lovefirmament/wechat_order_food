package com.pers.food.object.po;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pers.food.util.DateToLongSerializer;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class ProductCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String categoryId;
    private String categoryName;
    private Integer categoryType;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date createTime;
    @JsonSerialize(using= DateToLongSerializer.class)
    private Date updateTime;
}
