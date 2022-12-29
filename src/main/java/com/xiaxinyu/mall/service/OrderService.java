package com.xiaxinyu.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.filter.UserFilter;
import com.xiaxinyu.mall.model.request.CreateOrderReq;
import com.xiaxinyu.mall.model.vo.CartVO;
import com.xiaxinyu.mall.model.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNum);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);

    void cancel(String orderNo);

    String qrcode(String orderNo);
}
