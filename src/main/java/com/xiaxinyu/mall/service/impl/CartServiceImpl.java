package com.xiaxinyu.mall.service.impl;

import com.xiaxinyu.mall.common.Constant;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.model.dao.CartMapper;
import com.xiaxinyu.mall.model.dao.ProductMapper;
import com.xiaxinyu.mall.model.pojo.Cart;
import com.xiaxinyu.mall.model.pojo.Product;
import com.xiaxinyu.mall.model.vo.CartVO;
import com.xiaxinyu.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月09日 11:58
 * @Copyright:
 * @version: 1.0.0
 */

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        return cartVOS.stream().peek(cartVO -> cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setQuantity(count);
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }


    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MyException(ExceptionEnum.UPDATE_FAILED);
        } else {
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setQuantity(count);
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MyException(ExceptionEnum.DELETE_FAILED);
        } else {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }


    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new MyException(ExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if (count > product.getStock()) {
            throw new MyException(ExceptionEnum.NOT_ENOUGH);
        }
    }

}