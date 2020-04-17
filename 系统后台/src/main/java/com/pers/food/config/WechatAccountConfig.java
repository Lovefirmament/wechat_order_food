package com.pers.food.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;

    private String openAppId;

    private String openAppSecret;

    private String mchId;
    //商户密钥
    private String mchKey;
    //商户证书路径
    private String keyPath;

    private String notifyUrl;

}
