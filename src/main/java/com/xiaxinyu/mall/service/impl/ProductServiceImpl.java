package com.xiaxinyu.mall.service.impl;

import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.dao.ProductMapper;
import com.xiaxinyu.mall.model.pojo.Product;
import com.xiaxinyu.mall.model.request.AddProductReq;
import com.xiaxinyu.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月07日 13:16
 * @Copyright:
 * @version: 1.0.0
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if(productOld != null){
            throw new MyException(ExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if(count != 1){
            throw new MyException(ExceptionEnum.CREATE_FAILED);
        }
    }
}