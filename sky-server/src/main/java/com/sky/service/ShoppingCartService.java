package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {


    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void addshoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 展示购物车数据
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @author paxi
     * @data 2023/9/5
     **/
    List<ShoppingCart> showShoppingCart();

    /**
     * 删除购物车中的单个商品数据
     * @param shoppingCartDTO
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void deleteOne(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车信息
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void deleteShoppingCart();
}
