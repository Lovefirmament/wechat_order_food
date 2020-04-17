package com.pers.food.Aspect;


import com.pers.food.constant.RedisConstant;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.UserException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.pers.food.controller.Seller*.*(..)) && !execution(public * com.pers.food.controller.SellerUserController.*(..))")
    public void verify(){}

    @Before("verify()")
    public void doverify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //从http请求头中拿到token
        String token=request.getHeader("Authorization");
        if(token==null)
        {
            throw new UserException(ResultEnum.LOGIN_STATE_ERROR);
        }
        String tokenValue= redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,token));
        if(StringUtils.isEmpty(tokenValue)){
            throw new UserException(ResultEnum.TOKEN_NOT_FOUND);
        }
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),tokenValue,RedisConstant.EXPIRE, TimeUnit.SECONDS);
    }
}
