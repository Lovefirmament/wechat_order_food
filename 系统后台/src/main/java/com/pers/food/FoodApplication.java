package com.pers.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "com.pers.food.mapper")
public class FoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodApplication.class, args);
    }

}
