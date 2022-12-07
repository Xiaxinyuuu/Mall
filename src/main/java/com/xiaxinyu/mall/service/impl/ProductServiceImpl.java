package com.xiaxinyu.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.dao.ProductMapper;
import com.xiaxinyu.mall.model.pojo.Product;
import com.xiaxinyu.mall.model.request.AddProductReq;
import com.xiaxinyu.mall.model.request.UpdateProductReq;
import com.xiaxinyu.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new MyException(ExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count != 1) {
            throw new MyException(ExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product productOld = productMapper.selectByName(updateProductReq.getName());
        //同名且不同id，不能继续修改
        if (productOld != null && productOld.getId().equals(updateProductReq.getId())) {
            throw new MyException(ExceptionEnum.NAME_EXISTED);
        }
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count != 1)
            throw new MyException(ExceptionEnum.UPDATE_FAILED);
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) {
            throw new MyException(ExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count != 1)
            throw new MyException(ExceptionEnum.DELETE_FAILED);
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer status) {
        productMapper.batchUpdateSellStatus(ids,status);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }
}