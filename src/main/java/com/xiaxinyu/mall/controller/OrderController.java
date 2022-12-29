package com.xiaxinyu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.model.request.CreateOrderReq;
import com.xiaxinyu.mall.model.vo.OrderVO;
import com.xiaxinyu.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月29日 15:36
 * @Copyright: 个人版权所有
 * @version: 1.0.0
 */

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("order/create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @ApiOperation("前台订单详情")
    @GetMapping("order/detail")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @ApiOperation("前台订单列表")
    @GetMapping("order/list")
    public ApiRestResponse detail(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台取消订单")
    @PostMapping("order/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("生成支付二维码")
    @PostMapping("order/qrcode")
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @ApiOperation("支付接口")
    @GetMapping("pay")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }
}