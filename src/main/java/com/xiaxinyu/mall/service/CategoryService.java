package com.xiaxinyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.model.request.AddCategoryReq;
import com.xiaxinyu.mall.model.request.UpdateCategoryReq;
import com.xiaxinyu.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(UpdateCategoryReq updateCategoryReq);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer();
}
