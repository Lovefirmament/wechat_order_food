package com.pers.food.controller;

import com.github.pagehelper.PageInfo;
import com.pers.food.enums.ResultEnum;
import com.pers.food.exception.ProductException;
import com.pers.food.object.po.ProductCategory;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.service.ProductService;
import com.pers.food.util.KeyUtil;
import com.pers.food.util.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/productinfolist")
    public ResultVO getProductinfolist(@RequestParam(value = "pageNum",defaultValue = "1") Integer page,@RequestParam(value = "pageSize",defaultValue = "5") Integer size,String query, String property){
        PageInfo productPageInfo=productService.getProductAll(property,query,page,size);
        return ResultUtils.success(productPageInfo);
    }
    @GetMapping("/productinfo/{productId}")
    public ResultVO getProductInfo(@PathVariable String productId){
        ProductInfo productInfo=productService.getProductOne(productId);
        return ResultUtils.success(productInfo);
    }
    @GetMapping("/productcategoryall")
    @Cacheable(cacheNames = "productCategoryAll",key="1")
    public ResultVO getProductCategoryAll(){
        List<ProductCategory> productCategoryList=productService.getCategoryAll();
        return ResultUtils.success(productCategoryList);
    }
    @GetMapping("/productcategorylist")
    public ResultVO getProductCategory(@RequestParam(value = "pageNum",defaultValue = "1") Integer page,@RequestParam(value = "pageSize",defaultValue = "5") Integer size,String query, String property){
        PageInfo<ProductCategory> productCategoryList=productService.getCategoryAll(property,query,page,size);
        return ResultUtils.success(productCategoryList);
    }
    @GetMapping("/productcategory/{categoryId}")
    public ResultVO getProductCategory(@PathVariable String categoryId){
        ProductCategory productCategory=productService.getCategoryById(categoryId);
        return ResultUtils.success(productCategory);
    }
    @PostMapping("/productcategory")
    @CacheEvict(cacheNames = "productCategoryAll",key="1")
    public ResultVO insertProductCategory(@RequestBody ProductCategory productCategory){
        String categoryId=KeyUtil.genProductKey();
        productCategory.setCategoryId(categoryId);
        ProductCategory productResult=productService.insertCategory(productCategory);
        return ResultUtils.success(productResult);
    }
    @PutMapping("/productcategory")
    @CacheEvict(cacheNames = "productCategoryAll",key="1")
    public ResultVO updateProductCategory(@RequestBody ProductCategory productCategory){
        ProductCategory productCategory1=new ProductCategory();
        BeanUtils.copyProperties(productCategory,productCategory1,new String[]{"createTime","updateTime"});
        ProductCategory productResult=productService.updateCategory(productCategory1);
        return ResultUtils.success(productResult);
    }
    @DeleteMapping("/productcategory/{categoryId}")
    @CacheEvict(cacheNames = "productCategoryAll",key="1")
    public ResultVO deleteroductCategory(@PathVariable String categoryId){
        int productResult=productService.deleteCategory(categoryId);
        if(productResult==0)
        return ResultUtils.success("删除成功");
        else{
            return  ResultUtils.fail("删除失败，商品信息中存在该类目");
        }
    }
    //新增商品
    @PostMapping("/productinfo")
    public ResultVO insertProductInfo(@RequestBody ProductInfo productInfo){
        String productId=KeyUtil.genProductKey();
        productInfo.setProductId(productId);
        ProductInfo productResult=productService.insertProduct(productInfo);
        return ResultUtils.success(productResult);
    }
    //更新商品
    @PutMapping("/productinfo")
    public ResultVO updateProductInfo(@RequestBody ProductInfo productInfo){
        ProductInfo productInfo1=new ProductInfo();
        BeanUtils.copyProperties(productInfo,productInfo1,new String[]{"createTime","updateTime"});
        ProductInfo productResult=productService.updateProduct(productInfo1);
        return ResultUtils.success(productResult);
    }
    //删除商品
    @DeleteMapping("/productinfo/{productId}")
    public ResultVO deleteProductInfo(@PathVariable String productId){
        String productResult=productService.deleteProduct(productId);
        return ResultUtils.success(productResult);
    }
    //更新商品状态
    @PutMapping("/productStatus/{productId}/{productStatus}")
    public ResultVO productStatus(@PathVariable String productId,@PathVariable Integer productStatus){
        ProductInfo productInfo=null;
        if(productStatus==0){
            productInfo=productService.onSale(productId);
        }else if(productStatus==1){
            productInfo=productService.offSale(productId);
        }else{
            throw new ProductException(ResultEnum.PRODUCT_STATE_ERROR);
        }
        return ResultUtils.success(productInfo);
    }
}
