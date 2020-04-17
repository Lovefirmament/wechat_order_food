package com.pers.food.util;


import com.pers.food.object.vo.ResultVO;

public class ResultUtils {
    public static ResultVO success(Object object){
        ResultVO resultVo=new ResultVO();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        resultVo.setData(object);
        return resultVo;
    }
    public static ResultVO success(){
        return success(null);
    }


    public static ResultVO fail(Object object){
        ResultVO resultVo=new ResultVO();
        resultVo.setCode(1);
        resultVo.setMsg("失败");
        resultVo.setData(object);
        return resultVo;
    }

    public static ResultVO authorizeFail(){
        ResultVO resultVo=new ResultVO();
        resultVo.setCode(400);
        resultVo.setMsg("授权失败");
        return resultVo;
    }
}
