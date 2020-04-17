package com.pers.food.util;

import java.util.Random;

public class KeyUtil {
    public static synchronized String genUniqueKey(){
        Random random=new Random();
        Integer number=random.nextInt(900000)+100000;
        return System.currentTimeMillis()+String.valueOf(number);
    }
    public static synchronized String genProductKey(){
        Random random=new Random();
        Integer number=random.nextInt(90)+10;
        return System.currentTimeMillis()+String.valueOf(number);
    }
}
