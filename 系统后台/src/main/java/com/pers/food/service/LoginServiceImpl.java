package com.pers.food.service;

import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import com.pers.food.mapper.SellerInfoMapper;
import com.pers.food.object.po.SellerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private SellerInfoMapper sellerInfoMapper;
    @Override
    public SellerInfo getSellerInfo(String username,String password) {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }else {
            SellerInfo sellerInfo = new SellerInfo();
            sellerInfo.setUsername(username);
            sellerInfo.setPassword(password);
            return sellerInfoMapper.selectOne(sellerInfo);
        }
    }
}
