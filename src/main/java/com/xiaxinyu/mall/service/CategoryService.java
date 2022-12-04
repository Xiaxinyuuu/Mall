package com.xiaxinyu.mall.service;

import com.xiaxinyu.mall.model.request.AddCategoryReq;
import com.xiaxinyu.mall.model.request.UpdateCategoryReq;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq);

    void update(UpdateCategoryReq updateCategoryReq);
}
