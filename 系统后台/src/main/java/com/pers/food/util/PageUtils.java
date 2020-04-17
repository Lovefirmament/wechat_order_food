package com.pers.food.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.beans.Beans;
import java.util.List;

public class PageUtils{

     public static <P, V> PageInfo<V> PageInfoToPageInfoVo(PageInfo<P> pageInfoPo, List<V> VOlist) {
         PageInfo<V> pageInfoVO = new PageInfo<>();
         BeanUtils.copyProperties(pageInfoPo,pageInfoVO,"list");
         Page page=new Page(pageInfoPo.getPageNum(),pageInfoVO.getPageSize());
         page.setTotal(pageInfoPo.getTotal());
         for (V v : VOlist) {
             page.add(v);
         }
         pageInfoVO.setList(page);
         return pageInfoVO;
     }
}