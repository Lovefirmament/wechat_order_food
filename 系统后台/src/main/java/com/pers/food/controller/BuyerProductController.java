package com.pers.food.controller;



import com.pers.food.object.po.ProductCategory;
import com.pers.food.object.po.ProductInfo;
import com.pers.food.object.vo.ProductInfoVO;
import com.pers.food.object.vo.ProductVO;
import com.pers.food.object.vo.ResultVO;
import com.pers.food.service.ProductService;
import com.pers.food.util.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/test")
    public void test(HttpServletRequest request, HttpSession httpSession)
    {
        System.out.println(request);
        System.out.println(httpSession);
    }
    @GetMapping("/list")
    public ResultVO list() {
        //获取所有的productInfo
        List<ProductInfo> productInfoList = productService.getProductUpAll();
        //获取productInfo中所有的categoryType
        List<Integer> categoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.toList());
        //根据categoryType查询categoryList
        List<ProductCategory> categoryList = productService.getProductCategoryInType(categoryTypeList);
        //组装数据
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        for (ProductCategory category : categoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(category.getCategoryName());
            productVO.setCategoryType(category.getCategoryType());
            List<ProductInfoVO> productInfoVoList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(category.getCategoryType())) {
                    ProductInfoVO productInfoVo = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            }
            productVO.setProductInfoVOList(productInfoVoList);
            productVOList.add(productVO);
        }
        ResultVO resultVO = ResultUtils.success(productVOList);
        return resultVO;
    }


}
