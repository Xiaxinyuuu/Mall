package com.xiaxinyu.mall.service;

import com.xiaxinyu.mall.model.vo.CartVO;

import java.util.List;

public interface CartService {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count);
}
