package com.pers.food.exception;

import com.pers.food.enums.ResultEnum;

public class UserException extends RuntimeException {
    private int code;
    public UserException(int code,String message){
        super(message);
        this.code=code;
    }
    public UserException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code=resultEnum.getCode();
    }
}
