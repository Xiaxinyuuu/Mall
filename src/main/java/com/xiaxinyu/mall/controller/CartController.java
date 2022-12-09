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
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOS = cartService.add(UserFilter.user.getId(), productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOS = cartService.update(UserFilter.user.getId(), productId, count);
        return ApiRestResponse.success(cartVOS);
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车")
    public ApiRestResponse delete(@RequestParam Integer productId){
        List<CartVO> cartVOS = cartService.delete(UserFilter.user.getId(), productId);
        return ApiRestResponse.success(cartVOS);
    }


    @PostMapping("/select")
    @ApiOperation("选择/不选择购物车的某商品")
    public ApiRestResponse select(@RequestParam Integer productId,@RequestParam Integer selected){
        List<CartVO> cartVOS = cartService.selectOrNot(UserFilter.user.getId(), productId, selected);
        return ApiRestResponse.success(cartVOS);
    }


    @PostMapping("/selectAll")
    @ApiOperation("选择/不选择购物车的某商品")
    public ApiRestResponse selectAll(@RequestParam Integer selected){
        List<CartVO> cartVOS = cartService.selectAllOrNot(UserFilter.user.getId(), selected);
        return ApiRestResponse.success(cartVOS);
    }

    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list(){
        List<CartVO> cartVOS = cartService.list(UserFilter.user.getId());
        return ApiRestResponse.success(cartVOS);
    }
}