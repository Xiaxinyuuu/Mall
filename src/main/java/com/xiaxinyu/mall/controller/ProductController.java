package com.xiaxinyu.mall.controller;

import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月07日 15:41
 * @Copyright:
 * @version: 1.0.0
 */

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;


    @ApiOperation("商品详情")
    @PostMapping("product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        return ApiRestResponse.success(productService.detail(id));
    }
}