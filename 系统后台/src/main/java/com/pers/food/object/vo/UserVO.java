package com.pers.food.object.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 9098717301875076528L;
    private String username;
    private String token;
}
