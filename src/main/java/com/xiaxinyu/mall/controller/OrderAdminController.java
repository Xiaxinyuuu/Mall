package com.xiaxinyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月29日 19:09
 * @Copyright:
 * @version: 1.0.0
 */

@RestController
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("管理员订单列表")
    @GetMapping("admin/order/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("管理员发货")
    @PostMapping("admin/order/delivered")
    public ApiRestResponse delivered(@RequestParam String orderNo){
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("完结订单")
    @PostMapping("order/finish")
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}