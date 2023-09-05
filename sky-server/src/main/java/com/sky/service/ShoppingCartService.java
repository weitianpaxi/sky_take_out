package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {


    /**
     * 购物车添加商品
     * @param shoppingCartDTO
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void addshoppingCart(ShoppingCartDTO shoppingCartDTO);
}
