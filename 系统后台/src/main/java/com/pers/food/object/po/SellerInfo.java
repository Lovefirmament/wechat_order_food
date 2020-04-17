package com.pers.food.object.po;

import lombok.Data;

import java.util.Date;

@Data
public class SellerInfo {
    private String sellerId;
    private String username;
    private String password;
    private String openid;
    private Date createTime;
    private Date updateTime;
}
