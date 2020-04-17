package com.pers.food.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pers.food.enums.ProductStatesEnum;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.ProductException;
import com.pers.food.mapper.ProductCategoryMapper;
import com.pers.food.mapper.ProductInfoMapper;
import com.pers.food.object.dto.CartDTO;
import com.pers.food.object.dto.OrderDTO;
import com.pers.food.object.po.OrderMaster;
import com.pers.food.object.po.ProductCategory;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.object.vo.ProductInfoVO;
import com.pers.food.object.vo.ProductSellerVO;
import com.pers.food.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    @Override
    public ProductCategory getCategoryById(String categoryId) {
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryId(categoryId);
        return productCategoryMapper.selectOne(productCategory);
    }
    @Override
    public ProductCategory getCategoryByType(Integer categoryType) {
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryType(categoryType);
        return productCategoryMapper.selectOne(productCategory);
    }

    @Override
    public ProductCategory updateCategory(ProductCategory productCategory) {
        int updateResult=productCategoryMapper.updateByPrimaryKeySelective(productCategory);
        if(updateResult==0){
            throw new ProductException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
        return getCategoryById(productCategory.getCategoryId());
    }
    @Override
    public ProductCategory insertCategory(ProductCategory productCategory){
        int insertResult=productCategoryMapper.insertSelective(productCategory);
        if(insertResult==0){
            throw new ProductException(ResultEnum.PRODUCT_INSERT_ERROR);
        }
        return getCategoryById(productCategory.getCategoryId());
    }
    @Override
    public int deleteCategory(String categoryId){
        if(!StringUtils.isEmpty(categoryId)) {
            List<ProductInfo> productInfoList=productInfoMapper.selectAll();
            List<Integer> categoryTypeList=productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());
            List<ProductCategory> categoryList = getProductCategoryInType(categoryTypeList);
            for (ProductCategory productCategory : categoryList) {
                if(productCategory.getCategoryId().equals(categoryId)){
                    return 1; //删除失败，原因是商品信息中已存在该类目
                }
            }
            int deleteResult = productCategoryMapper.deleteByPrimaryKey(categoryId);
            if(deleteResult==0){
                throw new ProductException(ResultEnum.PRODUCT_DELETE_ERROR);
            }
        }else{
            throw new ProductException(ResultEnum.PRODUCT_DELETE_ERROR);
        }
        return 0; //删除成功
    }
    @Override
    public List<ProductCategory> getCategoryAll() {
        List<ProductCategory> productCategoryList=productCategoryMapper.selectAll();
        return productCategoryList;
    }
    @Override
    public PageInfo<ProductCategory> getCategoryAll(String property,String query,Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<ProductCategory> productCategoryList=null;
        if((!StringUtils.isEmpty(property))&&(!StringUtils.isEmpty(query))) {
            Example example=new Example(ProductCategory.class);
            String condition = "%" + query + "%";
            example.createCriteria().andLike(property, condition);
            productCategoryList=productCategoryMapper.selectByExample(example);
        }
        else{
            productCategoryList=productCategoryMapper.selectAll();
        }
        PageInfo<ProductCategory> pageInfoList=new PageInfo<>(productCategoryList);
        return pageInfoList;
    }
    @Override
    public List<ProductCategory> getProductCategoryInType(List<Integer> typeList) {
        List<ProductCategory> productCategoryList=productCategoryMapper.selectCategoryInType(typeList);
        return productCategoryList;
    }
    @Override
    public List<ProductInfo> getProductUpAll() {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductStatus(0);
        List<ProductInfo> productInfoList=productInfoMapper.select(productInfo);
        return productInfoList;
    }

    @Override
    public ProductInfo updateProduct(ProductInfo productInfo) {

        int updateResult=productInfoMapper.updateByPrimaryKeySelective(productInfo);
        if(updateResult==0){
            throw new ProductException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
        return  getProductOne(productInfo.getProductId());
    }
    @Override
    public ProductInfo insertProduct(ProductInfo productInfo) {

        int insertResult=productInfoMapper.insertSelective(productInfo);
        if(insertResult==0){
            throw new ProductException(ResultEnum.PRODUCT_INSERT_ERROR);
        }
        return  getProductOne(productInfo.getProductId());
    }
    @Override
    public String deleteProduct(String productId) {
        ProductInfo productInfo=new ProductInfo();
        if(!StringUtils.isEmpty(productId)){
        productInfo.setProductId(productId);
        int deleteResult=productInfoMapper.delete(productInfo);
        if(deleteResult==0){
            throw new ProductException(ResultEnum.PRODUCT_DELETE_ERROR);
        }}else{
            throw new ProductException(ResultEnum.PRODUCT_DELETE_ERROR);
        }
        return  "删除成功";
    }

    @Override
    public List<ProductInfo> getProductInfoInId(List<String> productIdList) {
        List<ProductInfo> productInfoList=productInfoMapper.selectProductInfoInId(productIdList);
        return productInfoList;
    }


    @Override
    public ProductInfo getProductOne(String productId) {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId(productId);
        return productInfoMapper.selectOne(productInfo);
    }

    @Override
    public PageInfo<ProductSellerVO> getProductAll(String property,String query,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ProductInfo> productInfoList=null;
        if((!StringUtils.isEmpty(property))&&(!StringUtils.isEmpty(query))) {
            Example example=new Example(ProductInfo.class);
            String condition = "%" + query + "%";
            example.createCriteria().andLike(property, condition);
            productInfoList=productInfoMapper.selectByExample(example);
        }
        else{
            productInfoList=productInfoMapper.selectAll();
        }
        PageInfo<ProductInfo> pageInfoList=new PageInfo<>(productInfoList);
        List<ProductSellerVO> productSellerVOList=new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            ProductSellerVO productSellerVO=new ProductSellerVO();
            ProductCategory productCategory=getCategoryByType(productInfo.getCategoryType());
            BeanUtils.copyProperties(productInfo,productSellerVO);
            productSellerVO.setCategoryName(productCategory.getCategoryName());
            productSellerVOList.add(productSellerVO);
        }
        PageInfo<ProductSellerVO> pageInfoTransfer= PageUtils.PageInfoToPageInfoVo(pageInfoList,productSellerVOList);
        return pageInfoTransfer;
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo=new ProductInfo();
            productInfo.setProductId(cartDTO.getProductId());
            ProductInfo productResult=productInfoMapper.selectOne(productInfo);
            //判断商品是否存在
            if(productResult==null){
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock=productResult.getProductStock()+cartDTO.getProductQuantity();

            productResult.setProductStock(stock);
            productInfoMapper.updateByPrimaryKeySelective(productResult);
        }
    }

    //扣库存
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList){
        for(CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo=new ProductInfo();
            productInfo.setProductId(cartDTO.getProductId());
            ProductInfo productResult=productInfoMapper.selectOne(productInfo);
            //判断商品是否存在
            if(productResult==null){
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock=productResult.getProductStock()-cartDTO.getProductQuantity();
            //判断库存是否充足
            if(stock<0){
                throw new ProductException(ResultEnum.PRODUCT_OUT_OF_STOCK);
            }
            productResult.setProductStock(stock);
            productInfoMapper.updateByPrimaryKeySelective(productResult);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfoQuery=new ProductInfo();
        productInfoQuery.setProductId(productId);
        ProductInfo productInfo=productInfoMapper.selectOne(productInfoQuery);
        if(productInfo==null){
            throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatus().equals(ProductStatesEnum.UP.getCode()))
        {
            throw new ProductException(ResultEnum.PRODUCT_STATE_ERROR);
        }
        productInfo.setProductStatus(0);
        int result=productInfoMapper.updateByPrimaryKeySelective(productInfo);
        if(result==0){
            throw new ProductException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
        return getProductOne(productId);
    }

    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfoQuery=new ProductInfo();
        productInfoQuery.setProductId(productId);
        ProductInfo productInfo=productInfoMapper.selectOne(productInfoQuery);
        if(productInfo==null){
            throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatus().equals(ProductStatesEnum.down.getCode()))
        {
            throw new ProductException(ResultEnum.PRODUCT_STATE_ERROR);
        }
        productInfo.setProductStatus(1);
        int result=productInfoMapper.updateByPrimaryKeySelective(productInfo);
        if(result==0){
            throw new ProductException(ResultEnum.PRODUCT_UPDATE_ERROR);
        }
        return getProductOne(productId);
    }
}
