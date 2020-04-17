package com.pers.food.handler;

import com.pers.food.exception.UserException;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.util.ResultUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SellExceptionHandler {

    //拦截登陆异常
    @ExceptionHandler(value=UserException.class)
    @ResponseBody
    public ResultVO handleLoginExcpetion()
    {
     return ResultUtils.authorizeFail();
    }
}
