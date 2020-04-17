package com.pers.food.controller;

import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;
    //获得code,这个returnurl是前端的地址
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnurl){
        String url="http://zhmb.natapp1.cc/sell/wechat/userInfo";
        //这个方法的参数设置了链接中的回调url，scope，和state，返回的是微信的授权链接
        String redirectUrl=wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnurl));
        return "redirect:"+redirectUrl;

    }
    //重定向到授权链接之后会继续执行下面的方法
    @GetMapping("/userInfo")
    public String userinfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken=new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("微信网页授权{}",e);
            throw new OrderException(ResultEnum.WECHAT_MP_ERROR);
        }
        String openid=wxMpOAuth2AccessToken.getOpenId();

        return "redirect:"+returnUrl+"?openid=oTgZpwTyxKbcRBQRa6X3We6K9nzk";
    }
}
