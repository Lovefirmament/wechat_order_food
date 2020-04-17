package com.pers.food.mapper;



import com.pers.food.object.po.ProductCategory;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductCategoryMapper extends Mapper<ProductCategory> {
    public List<ProductCategory> selectCategoryInType(List<Integer> productType);
}
