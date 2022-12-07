package com.xiaxinyu.mall.controller;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月07日 13:10
 * @Copyright: 个人版权所有
 * @version: 1.0.0
 */

import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.model.request.AddProductReq;
import com.xiaxinyu.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 后台商品管理Controller
 */
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }
}