package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.vo.Cart;
import com.atguigu.gmall.cart.vo.CartItemVO;

import java.util.List;

public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCarts();

    void updateCart(Cart cart);

    void deleteCart(Long skuId);

    void checkCart(List<Cart> carts);

    List<CartItemVO> queryCartItemVO(Long userId);
}
