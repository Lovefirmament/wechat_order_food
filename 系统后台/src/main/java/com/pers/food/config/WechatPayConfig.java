package com.pers.food.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class WechatPayConfig {
    @Autowired
    private  WechatAccountConfig wechatAccountConfig;


    @Bean
    WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig=new WxPayConfig();
        wxPayConfig.setAppId(wechatAccountConfig.getMpAppId());
        wxPayConfig.setAppSecret(wechatAccountConfig.getMpAppSecret());
        wxPayConfig.setMchId(wechatAccountConfig.getMchId());
        wxPayConfig.setMchKey(wechatAccountConfig.getMchKey());
        wxPayConfig.setKeyPath(wechatAccountConfig.getKeyPath());
        wxPayConfig.setNotifyUrl(wechatAccountConfig.getNotifyUrl());
        return wxPayConfig;
    }
    @Bean
    public BestPayServiceImpl bestPayService(){

        BestPayServiceImpl bestPayService=new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig());
        return  bestPayService;
    }
}
