package com.pers.food.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.pers.food.object.dto.CartDTO;
import com.pers.food.object.po.ProductCategory;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.object.vo.ProductSellerVO;

import java.util.List;

public interface ProductService {
    //查找一个类目
    ProductCategory getCategoryById(String categoryId);
    ProductCategory getCategoryByType(Integer Type);
    //更新类目
    ProductCategory updateCategory(ProductCategory productCategory);
    //新增类目
    ProductCategory insertCategory(ProductCategory productCategory);
    //删除类目
    int deleteCategory(String categoryId);
    //查询所有类目
    List<ProductCategory> getCategoryAll();
    //分页查询商品类目
    PageInfo<ProductCategory> getCategoryAll(String property,String query,Integer page, Integer size);
    //根据类型集合查询商品类目
    List<ProductCategory> getProductCategoryInType(List<Integer> typeList);
    //查找一件商品
    ProductInfo getProductOne(String productId);
    //分页查询所有商品
    PageInfo<ProductSellerVO> getProductAll(String property,String query,Integer page, Integer size);
    //查询上架的所有商品
    List<ProductInfo> getProductUpAll();
    //商品更新
    ProductInfo updateProduct(ProductInfo productInfo);
    //商品新增
    ProductInfo insertProduct(ProductInfo productInfo);
    //商品删除
    String deleteProduct(String ProductId);
    //根据Id获取商品信息
    List<ProductInfo> getProductInfoInId(List<String> productIdList);
    //增库存
    void increaseStock(List<CartDTO> cartDTOList);
    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);
    //上架
    ProductInfo onSale(String productId);
    //下架
    ProductInfo offSale(String productId);
}
