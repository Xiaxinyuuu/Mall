package com.xiaxinyu.mall.controller;

import com.xiaxinyu.mall.common.ApiRestResponse;
import com.xiaxinyu.mall.filter.UserFilter;
import com.xiaxinyu.mall.model.vo.CartVO;
import com.xiaxinyu.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月09日 11:43
 * @Copyright:
 * @version: 1.0.0
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车接口")
    public ApiRestResponse add(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOS = cartService.add(UserFilter.user.getId(), productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list(){
        List<CartVO> cartVOS = cartService.list(UserFilter.user.getId());
        return ApiRestResponse.success(cartVOS);
    }
}