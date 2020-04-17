package com.pers.food.object.form;

import com.pers.food.object.dto.CartDTO;
import com.pers.food.object.po.OrderDetail;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class OrderForm {

    @NotEmpty(message = "姓名必填")
    private String name;
    @NotEmpty(message = "手机号必填")
    private String phone;
    @NotEmpty(message = "地址必填")
    private String address;
    @NotEmpty(message = "openId必填")
    private String openid;
    @NotEmpty(message = "购物车不能为空")
    private String items;

}
