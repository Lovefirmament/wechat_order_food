package com.pers.food.service;

import com.pers.food.object.po.SellerInfo;

public interface LoginService {
    SellerInfo getSellerInfo(String username,String password);
}
