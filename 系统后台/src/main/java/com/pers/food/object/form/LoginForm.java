package com.pers.food.object.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
}
