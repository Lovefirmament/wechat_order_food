package com.pers.food.controller;

import com.pers.food.constant.RedisConstant;
import com.pers.food.object.form.LoginForm;
import com.pers.food.object.po.SellerInfo;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.object.vo.UserVO;
import com.pers.food.service.LoginService;
import com.pers.food.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seller/user")
@CrossOrigin
public class SellerUserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginForm loginForm){
        SellerInfo sellerInfo=loginService.getSellerInfo(loginForm.getUsername(),loginForm.getPassword());
        UserVO userVo=new UserVO();
        //生成token
        if(sellerInfo!=null){
            String token = UUID.randomUUID().toString();
            userVo.setUsername(sellerInfo.getUsername());
            userVo.setToken(token);
            //设置token到redis
            Integer expire= RedisConstant.EXPIRE;
            redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),sellerInfo.getUsername(),expire,TimeUnit.SECONDS);
            return ResultUtils.success(userVo);
        }
        return ResultUtils.fail(userVo);
    }

}
