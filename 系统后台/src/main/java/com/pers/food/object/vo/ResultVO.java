package com.pers.food.object.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 4831296233847657566L;
    private Integer code;
    private String msg;
    private T data;
}
