package com.xiaxinyu.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.dao.CategoryMapper;
import com.xiaxinyu.mall.model.pojo.Category;
import com.xiaxinyu.mall.model.request.AddCategoryReq;
import com.xiaxinyu.mall.model.request.UpdateCategoryReq;
import com.xiaxinyu.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月04日 19:54
 * @Copyright:
 * @version: 1.0.0
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if(categoryOld != null){
            throw new MyException(ExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if(count != 1)
            throw new MyException(ExceptionEnum.CREATE_FAILED);
    }

    @Override
    public void update(UpdateCategoryReq updateCategoryReq){
        if(! StringUtils.isEmpty(updateCategoryReq.getName())){
            Category categoryOld = categoryMapper.selectByName(updateCategoryReq.getName());
            if(categoryOld != null && !categoryOld.getId().equals(updateCategoryReq.getId())){
                throw new MyException(ExceptionEnum.NAME_EXISTED);
            }
        }
        Category newCategory = new Category();
        BeanUtils.copyProperties(updateCategoryReq,newCategory);
        int count = categoryMapper.updateByPrimaryKeySelective(newCategory);
        if(count != 1)
            throw new MyException(ExceptionEnum.UPDATE_FAILED);
    }

    @Override
    public void delete(Integer id){
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if(categoryOld == null)
            throw new MyException(ExceptionEnum.DELETE_FAILED);

        int count = categoryMapper.deleteByPrimaryKey(id);
        if(count != 1)
            throw new MyException(ExceptionEnum.DELETE_FAILED);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }
}