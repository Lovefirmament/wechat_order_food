package com.pers.food.mapper;


import com.pers.food.object.po.ProductInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface ProductInfoMapper extends Mapper<ProductInfo> {

    //根据id集合找到商品信息
    List<ProductInfo> selectProductInfoInId(List<String> productIdList);
}
